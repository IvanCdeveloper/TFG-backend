package com.example.tienda_reparaciones.utils;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Clase que añade a la respuesta las distintas opciones que tiene el usuario para
 * pasar de pagina y las distintas acciones que tiene. Solo disponible en respuestas
 * que utilicen paginación
 *
 *
 * @author Iván Cuarteros
 * @version 1.0
 * @since 2025-03-01
 */

@Component
public class PaginationLinksUtils {

    public String createLinkHeader(Page<?> page, UriComponentsBuilder uriBuilder) {
        final StringBuilder linkHeader = new StringBuilder();
        linkHeader.append("");

        if (page.hasNext()) {
            String uri = constructUri(page.getNumber()+1, page.getSize(), uriBuilder);
            linkHeader.append(buildLinkHeader(uri, "next"));
        }

        if (page.hasPrevious()) {
            String uri = constructUri(page.getNumber()-1, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "prev"));
        }

        if (!page.isFirst()) {
            String uri = constructUri(0, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "first"));
        }

        if (!page.isLast()) {
            String uri = constructUri(page.getTotalPages()-1, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "last"));
        }


        return linkHeader.toString();
    }

    private String constructUri(int newPageNumber, int size, UriComponentsBuilder uriBuilder) {
        return uriBuilder.replaceQueryParam("page", newPageNumber ).replaceQueryParam("size", size).build().encode().toUriString();
    }


    private String buildLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    private void appendCommaIfNecessary(final StringBuilder linkHeader) {
        if (!linkHeader.isEmpty()) {
            linkHeader.append(", ");
        }
    }

}
