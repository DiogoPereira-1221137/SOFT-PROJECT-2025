package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.mappers;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.model.elasticsearch.PhotoES;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Mapper(componentModel = "spring")
public interface PhotoMapperES {

    default PhotoES map(Photo photo) {
        if (photo == null || photo.getPhotoFile() == null) return null;
        try {
            return new PhotoES(Path.of(photo.getPhotoFile()));
        } catch (InvalidPathException e) {
            return null;
        }
    }

    default String mapPhotoESToString(PhotoES photoES) {
        return (photoES != null) ? photoES.getPhotoFile() : null;
    }

    default Photo mapPhotoESToPhoto(PhotoES photoES) {
        if (photoES == null || photoES.getPhotoFile() == null) return null;
        return new Photo(Path.of(photoES.getPhotoFile()));
    }



    default PhotoES mapPhotoToPhotoES(Photo photo) {
        if (photo == null || photo.getPhotoFile() == null) return null;
        return new PhotoES(Path.of(photo.getPhotoFile()));
    }

    default PhotoES mapStringToPhotoES(String photoURI) {
        if (photoURI == null) return null;
        try {
            return new PhotoES(Path.of(photoURI));
        } catch (InvalidPathException e) {
            return null;
        }
    }

    default String photoToString(Photo photo) {
        return (photo != null) ? photo.getPhotoFile() : null;
    }

    default Photo photoFromString(String photoURI) {
        if (photoURI == null) return null;
        try {
            return new Photo(Path.of(photoURI));
        } catch (InvalidPathException e) {
            return null;
        }
    }

}