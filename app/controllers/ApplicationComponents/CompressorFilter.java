package controllers.ApplicationComponents;

import com.mohiva.play.htmlcompressor.HTMLCompressorFilter;
import com.mohiva.play.xmlcompressor.XMLCompressorFilter;
import play.http.HttpFilters;
import play.mvc.EssentialAction;
import play.mvc.EssentialFilter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CompressorFilter extends EssentialFilter implements HttpFilters {

    private HTMLCompressorFilter htmlCompressorFilter;
    private XMLCompressorFilter xmlCompressorFilter;

    @Inject
    public CompressorFilter(
            HTMLCompressorFilter htmlCompressorFilter,
            XMLCompressorFilter xmlCompressorFilter) {

        this.htmlCompressorFilter = htmlCompressorFilter;
        this.xmlCompressorFilter = xmlCompressorFilter;
    }

    @Override
    public List<EssentialFilter> getFilters() {
        return new ArrayList<EssentialFilter>() {{
            add(htmlCompressorFilter.asJava());
            add(xmlCompressorFilter.asJava());
        }};
    }

    @Override
    public EssentialAction apply(EssentialAction next) {
        next = htmlCompressorFilter.apply(next).asJava();
        next = xmlCompressorFilter.apply(next).asJava();
        return next.asJava();
    }
}