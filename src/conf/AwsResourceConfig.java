package conf;
// Currently this class is not used, it's for uploading image files to aws
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/aws-config.xml")
public class AwsResourceConfig {

}