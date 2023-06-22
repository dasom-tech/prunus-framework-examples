package i18n.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
public class I18nReq {
    long id;

    @NotEmpty(message = "{valid.i18nReq.name.NotEmpty}")
    String name;

    @Size(min = 5, max = 50, message = "{valid.i18nReq.address.Size}")
    String address;
}
