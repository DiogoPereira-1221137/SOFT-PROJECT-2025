package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.elasticsearch;


import co.elastic.clients.elasticsearch._types.aggregations.*;
import java.util.Map;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.elasticsearch.GenreES;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;
import pt.psoft.g1.psoftg1.lendingmanagement.model.elasticSearch.LendingES;
import pt.psoft.g1.psoftg1.shared.services.Page;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;



import co.elastic.clients.elasticsearch._types.aggregations.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Profile("es")
public interface SpringDataGenreESRepository extends
        ElasticsearchRepository<GenreES, String>,
        GenreRepoCustomES {

    Optional<GenreES> findByGenre(String genre);
}

interface GenreRepoCustomES {
    List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre();
    List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, Page page);
    List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate);
}

@RequiredArgsConstructor
class GenreRepoCustomESImpl implements GenreRepoCustomES {

    private final ElasticsearchOperations operations;

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        LocalDate now = LocalDate.now();
        LocalDate twelveMonthsAgo = now.minusMonths(12);

        // Query de intervalo de datas
        Query query = QueryBuilders.range(r -> r
                .field("startDate")
                .gte(JsonData.of(twelveMonthsAgo))
                .lte(JsonData.of(now))
        );

        // Agregação: termos por género + histograma mensal
        Aggregation genreAgg = new Aggregation.Builder()
                .terms(t -> t.field("genre.keyword"))
                .aggregations("monthly", new Aggregation.Builder()
                        .dateHistogram(dh -> dh
                                .field("startDate")
                                .calendarInterval(CalendarInterval.Month)
                        )
                        .build())
                .build();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .withAggregation("genre", genreAgg)
                .build();

        SearchHits<LendingES> hits = operations.search(nativeQuery, LendingES.class);

        AggregationsContainer<?> aggregations = hits.getAggregations();
        if (aggregations == null) return List.of();

        @SuppressWarnings("unchecked")
        Map<String, Aggregate> aggMap = (Map<String, Aggregate>) aggregations.aggregations();

        Aggregate genreAggregate = aggMap.get("genre");
        if (genreAggregate == null || !genreAggregate.isSterms()) return List.of();

        List<GenreLendingsPerMonthDTO> result = new ArrayList<>();

        for (StringTermsBucket genreBucket : genreAggregate.sterms().buckets().array()) {
            String genre = genreBucket.key().stringValue();
            Map<String, Aggregate> subAggs = genreBucket.aggregations();
            Aggregate monthlyAgg = subAggs.get("monthly");

            if (monthlyAgg == null || !monthlyAgg.isDateHistogram()) continue;

            for (DateHistogramBucket monthBucket : monthlyAgg.dateHistogram().buckets().array()) {
                ZonedDateTime zdt = ZonedDateTime.parse(monthBucket.keyAsString(), DateTimeFormatter.ISO_DATE_TIME);
                int year = zdt.getYear();
                int month = zdt.getMonthValue();
                List<GenreLendingsDTO> values = List.of(new GenreLendingsDTO(genre, monthBucket.docCount()));
                result.add(new GenreLendingsPerMonthDTO(year, month, values));
            }
        }
        return result;
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, Page page) {
        // Aqui podes criar uma agregação semelhante, mas em vez de histogramas, fazer sum/count por género
        // e dividir pelo número de dias do mês
        LocalDate firstOfMonth = month.withDayOfMonth(1);
        LocalDate lastOfMonth = month.withDayOfMonth(month.lengthOfMonth());

        Query query = QueryBuilders.range(r -> r
                .field("startDate")
                .gte(JsonData.of(firstOfMonth))
                .lte(JsonData.of(lastOfMonth))
        );

        Aggregation genreAgg = new Aggregation.Builder()
                .terms(t -> t.field("genre.keyword"))
                .build();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .withAggregation("genre", genreAgg)
                .build();

        SearchHits<LendingES> hits = operations.search(nativeQuery, LendingES.class);

        AggregationsContainer<?> aggs = hits.getAggregations();
        if (aggs == null) return List.of();

        @SuppressWarnings("unchecked")
        Map<String, Aggregate> aggMap = (Map<String, Aggregate>) aggs.aggregations();

        Aggregate genreAggregate = aggMap.get("genre");
        if (genreAggregate == null || !genreAggregate.isSterms()) return List.of();

        int daysInMonth = month.lengthOfMonth();
        List<GenreLendingsDTO> result = new ArrayList<>();

        for (StringTermsBucket bucket : genreAggregate.sterms().buckets().array()) {
            String genre = bucket.key().stringValue();
            double avg = (double) bucket.docCount() / daysInMonth;
            result.add(new GenreLendingsDTO(genre, avg));
        }

        // Paginação manual
        int fromIndex = Math.min((page.getNumber() - 1) * page.getLimit(), result.size());
        int toIndex = Math.min(fromIndex + page.getLimit(), result.size());
        return result.subList(fromIndex, toIndex);
    }

//    @Override
//    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
//        return List.of();
//    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
        Query query = QueryBuilders.bool(b -> b
                .must(QueryBuilders.range(r -> r
                        .field("startDate")
                        .gte(JsonData.of(startDate))
                        .lte(JsonData.of(endDate))
                ))
                .must(QueryBuilders.exists(e -> e.field("returnedDate")))
        );

        Aggregation avgDurationAgg = Aggregation.of(a -> a
                .avg(avg -> avg.field("durationDays"))
        );

        Aggregation monthlyAgg = Aggregation.of(dh -> dh
                .dateHistogram(dh2 -> dh2
                        .field("startDate")
                        .calendarInterval(CalendarInterval.Month)
                )
                .aggregations("avgDuration", avgDurationAgg)  // Adiciona uma agregação de cada vez
        );

        Aggregation genreAgg = Aggregation.of(t -> t
                .terms(term -> term
                        .field("genre.keyword")
                        .size(100)
                )
                .aggregations("monthly", monthlyAgg)  // Adiciona uma agregação de cada vez
        );


        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .withAggregation("genre", genreAgg)
                .build();

        SearchHits<LendingES> hits = operations.search(nativeQuery, LendingES.class);

        AggregationsContainer<?> aggregations = hits.getAggregations();
        if (aggregations == null) return List.of();

        @SuppressWarnings("unchecked")
        Map<String, Aggregate> aggMap = (Map<String, Aggregate>) aggregations.aggregations();

        Aggregate genreAggregate = aggMap.get("genre");
        if (genreAggregate == null || !genreAggregate.isSterms()) return List.of();

        List<GenreLendingsPerMonthDTO> result = new ArrayList<>();

        for (StringTermsBucket genreBucket : genreAggregate.sterms().buckets().array()) {
            String genre = genreBucket.key().stringValue();
            Aggregate monthlyAggResult = genreBucket.aggregations().get("monthly");
            if (monthlyAggResult == null || !monthlyAggResult.isDateHistogram()) continue;

            for (DateHistogramBucket monthBucket : monthlyAggResult.dateHistogram().buckets().array()) {
                ZonedDateTime zdt = ZonedDateTime.parse(monthBucket.keyAsString(), DateTimeFormatter.ISO_DATE_TIME);
                int year = zdt.getYear();
                int month = zdt.getMonthValue();
                double avgDuration = monthBucket.aggregations().get("avgDuration").avg().value();
                List<GenreLendingsDTO> values = List.of(new GenreLendingsDTO(genre, avgDuration));
                result.add(new GenreLendingsPerMonthDTO(year, month, values));
            }
        }

        return result;
    }
}