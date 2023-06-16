package nexacro.adaptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import prunus.persistence.data.pagination.PaginationConfig;

@Import(PaginationConfig.class)
@Configuration
public class AdaptorConfiguration {
}
