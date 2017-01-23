package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Image;
import no.ntnu.team5.minvakt.model.ImageModel;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Harald Floor Wilhelmsen on 20.01.2017.
 */
@Component
@Scope("prototype")
public class ImageAccess extends Access<Image, ImageModel> {

    @Override
    public ImageModel toModel(Image image) {
        ImageModel imageModel = new ImageModel();
        imageModel.setB64Content(image.getContent());
        imageModel.setContentType(image.getType());
        return imageModel;
    }

    public Image getById(int image_id) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Image where id = :id");
            query.setParameter("id", image_id);
            return (Image) query.uniqueResult();
        });
    }
}
