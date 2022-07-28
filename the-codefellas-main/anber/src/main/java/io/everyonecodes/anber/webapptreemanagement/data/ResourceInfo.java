package io.everyonecodes.anber.webapptreemanagement.data;

import java.util.List;
import java.util.Objects;

public class ResourceInfo {

    private String category;
    private String name;
    private String description;
    private String uri;
    private String restMethod;
    private List<String> allowedRoles;

    public ResourceInfo() {}

    public ResourceInfo(String category, String name, String description, String uri, String restMethod, List<String> allowedRoles) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.uri = uri;
        this.restMethod = restMethod;
        this.allowedRoles = allowedRoles;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRestMethod() {
        return restMethod;
    }

    public void setRestMethod(String restMethod) {
        this.restMethod = restMethod;
    }

    public List<String> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(List<String> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceInfo that = (ResourceInfo) o;
        return Objects.equals(category, that.category) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(uri, that.uri) && Objects.equals(restMethod, that.restMethod) && Objects.equals(allowedRoles, that.allowedRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, name, description, uri, restMethod, allowedRoles);
    }

    @Override
    public String toString() {
        return "ResourceInfo{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uri='" + uri + '\'' +
                ", restMethod='" + restMethod + '\'' +
                ", allowedRoles=" + allowedRoles +
                '}';
    }
}
