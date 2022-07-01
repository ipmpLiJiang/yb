package cc.mrbird.febs.common.utils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;

@Component
public class EntityUtil {
    private  MapperFacade mapperFacade;
    public EntityUtil(){
        DefaultMapperFactory.Builder builder = new DefaultMapperFactory.Builder();
        builder.mapNulls(false);
        mapperFacade = builder.build().getMapperFacade();
    }
    public <SRC, DEST extends Object> DEST getEntity(SRC src, DEST dest) {
        mapperFacade.map(src, dest);
        return dest;
    }
}
