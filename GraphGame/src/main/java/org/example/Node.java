package org.example;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.List;

public class Node implements Serializable {
    private String label;

    @JsonManagedReference
    private List<Edge> children;

    public Node(){}
    public Node(String name) {
        this.label = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Edge> getChildren() {
        return children;
    }

    public void setChildren(List<Edge> children) {
        this.children = children;
    }
}
