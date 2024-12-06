package io.yody.yodemy.elearning.web.rest.vm.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MetafieldRequest implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    private String key;

    @Size(max = 65535)
    private String value;

    @NotBlank
    @Size(max = 50)
    private String type;

    private String ownerResource;
    private Long ownerId;
    private String namespace;
    private String description;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerResource() {
        return ownerResource;
    }

    public void setOwnerResource(String ownerResource) {
        this.ownerResource = ownerResource;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
