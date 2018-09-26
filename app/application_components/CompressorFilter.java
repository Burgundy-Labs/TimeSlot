package application_components;

import akka.stream.Materializer;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import com.mohiva.play.htmlcompressor.HTMLCompressorFilter;
import play.Environment;
import play.Mode;
import play.api.Configuration;

import javax.inject.Inject;

public class CompressorFilter extends HTMLCompressorFilter {

    private Configuration configuration;
    private Environment environment;
    private Materializer mat;

    @Inject
    public CompressorFilter(
            Configuration configuration, Environment environment, Materializer mat) {

        this.configuration = configuration;
        this.environment = environment;
        this.mat = mat;
    }

    @Override
    public Configuration configuration() {
        return configuration;
    }

    @Override
    public HtmlCompressor compressor() {
        HtmlCompressor compressor = new HtmlCompressor();
        if (environment.mode() == Mode.DEV) {
            compressor.setPreserveLineBreaks(true);
        }
        compressor.setRemoveComments(true);
        compressor.setRemoveIntertagSpaces(true);
        compressor.setRemoveHttpProtocol(true);
        compressor.setRemoveHttpsProtocol(false);
        compressor.setCompressCss(true);
        return compressor;
    }

    @Override
    public Materializer mat() {
        return mat;
    }

}