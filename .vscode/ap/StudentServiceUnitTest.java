
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.jupiter.api.extension.ExtendWith;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.studinstructor.services.studentservices;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jboss.arquillian.container.test.api.RunAsClient;


@ExtendWith(ArquillianExtension.class)
public class StudentServiceUnitTest{

    private jakarta.ws.rs.client.Client client;

    @ArquillianResource
    private URL base;

    @Deployment(testable=false)
    public static WebArchive createDeployment(){
        return create(WebArchive.class).addClass(studentservices.class)
                    .addAsWebInfResource("C:/Users/Public/Documents/data-access/servlets/servlet/src/test/resources/arquillian.xml", "resources/arquillian.xml")
                    .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        
    }

    @BeforeEach
    public void prepareClient(){
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
    }

    @AfterEach
    public void destroyClient(){
        if(this.client !=  null)
            this.client.close();
    }

    @Test
    @RunAsClient
    public void teststudentservlet() throws Exception{
        final WebTarget greetingTarget = client.target(new  URL(base, "api/greeting/JakartaEE").toExternalForm());
        try (final Response greetingGetResponse = greetingTarget.request()
                .accept(MediaType.APPLICATION_JSON)
                .get()) {
                
            assertEquals(greetingGetResponse.getStatus(),200);
        }
    }
}