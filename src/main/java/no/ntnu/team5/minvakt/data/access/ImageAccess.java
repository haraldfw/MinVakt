package no.ntnu.team5.minvakt.data.access;

import no.ntnu.team5.minvakt.db.Image;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Harald Floor Wilhelmsen on 20.01.2017.
 */
@Component
@Scope("prototype")
public class ImageAccess extends Access<Image, Void> {

    @Override
    Void toModel(Image image) {
        throw new NotImplementedException();
    }

    public Image getById(int image_id) {
        return getDb().transaction(session -> {
            Query query = session.createQuery("from Image where id = :id");
            query.setParameter("id", image_id);
            return (Image) query.uniqueResult();
        });
    }
}
