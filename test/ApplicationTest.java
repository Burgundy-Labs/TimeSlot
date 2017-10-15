import org.junit.Assert;
import org.junit.Test;
import play.twirl.api.Content;

/**
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 */
public class ApplicationTest {

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Test213");
        Assert.assertEquals(html.contentType(), "text/html");
        Assert.assertTrue(html.body().contains("Test213"));
    }
}
