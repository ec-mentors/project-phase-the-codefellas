package io.everyonecodes.anber.service;

import io.everyonecodes.anber.data.ResourceInfo;
import io.everyonecodes.anber.data.Role;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@ConfigurationProperties("webapptreelist")
public class WebAppTreeService {

    public String prepareWebAppTree() {
        return prepareOutput();
    }

    private List<ResourceInfo> resourceinfos;

    public void setResourceinfos(List<ResourceInfo> resourceinfos) {
        this.resourceinfos = resourceinfos;
    }

    private String prepareOutput() {
        var output = new StringBuilder();
        Collection<? extends GrantedAuthority> authorities = getAuthorities();

        Map<String, List<ResourceInfo>> resourceMap = resourceinfos.stream()
                .filter(element -> checkIfAuthoritiesAreInAllowedRoles(element.getAllowedRoles(), authorities))
                .collect(groupingBy(ResourceInfo::getCategory));

        for (Map.Entry<String, List<ResourceInfo>> mapEntry : resourceMap.entrySet()) {
            output
                    .append(mapEntry.getKey())
                    .append("\n");
            for (ResourceInfo resourceInfo : mapEntry.getValue()) {
                output
                        .append("    ")
                        .append(resourceInfo.getName())
                        .append("\n");
            }
        }
        return output.toString();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthentication().getAuthorities();
    }

    private boolean checkIfAuthoritiesAreInAllowedRoles(Set<Role> allowedRoles, Collection<? extends GrantedAuthority> authorities) {
        List<String> authoritiesStringList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        List<String> allowedRolesStringList = allowedRoles.stream()
                .map(Role::getRole)
                .collect(Collectors.toList());
        return authoritiesStringList.stream().anyMatch(allowedRolesStringList::contains);
    }
}
