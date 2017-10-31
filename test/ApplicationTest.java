import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.twirl.api.Content;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 */
public class ApplicationTest {

    @Before
    public void setUp() throws Exception {
        Http.Context context = mock(Http.Context.class);
        Http.Flash flash = mock(Http.Flash.class);
        Http.Session session = mock(Http.Session.class);
        when(context.session()).thenReturn(session);
        when(context.flash()).thenReturn(flash);
        Http.Context.current.set(context);
    }

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Test213");
        Assert.assertEquals(html.contentType(), "text/html");
        Assert.assertTrue(html.body().contains("Test213"));
    }
}
