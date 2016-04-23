package com.restbucks.ordering.representations;

import java.net.URI;
import java.net.URISyntaxException;

import com.restbucks.ordering.model.Identifier;

public class AppealsUri {
    private URI uri;
    
    public AppealsUri(String uri) {
        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    
    public AppealsUri(URI uri) {
        this(uri.toString());
    }

    public AppealsUri(URI uri, Identifier identifier) {
        this(uri.toString() + "/" + identifier.toString());
    }

    public int getGradeId() {
        String path = uri.getPath();
        return Integer.valueOf(path.substring(path.lastIndexOf("/") + 1, path.length()));
    }
    
    public Identifier getId() {
        String path = uri.getPath();
        return new Identifier(path.substring(path.lastIndexOf("/") + 1, path.length()));
    }

    public URI getFullUri() {
        return uri;
    }
    
    @Override
    public String toString() {
        return uri.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AppealsUri) {
            return ((AppealsUri)obj).uri.equals(uri);
        }
        return false;
    }

    public String getBaseUri() {
        /* // Old implementation
        String port = "";
        if(uri.getPort() != 80 && uri.getPort() != -1) {
            port = ":" + String.valueOf(uri.getPort());
        }
        
        return uri.getScheme() + "://" + uri.getHost() + port;
        * */
        
        String uriString = uri.toString();
        String baseURI   = uriString.substring(0, uriString.lastIndexOf("webresources/")+"webresources".length());
        
        return baseURI;
    }
}
