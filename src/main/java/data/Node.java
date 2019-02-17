package data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Node {
    @Id
    private UUID Id;

    private UUID Parent;

    private ArrayList<UUID> Children = new ArrayList<>();

    private Integer Height;

    private UUID Root;

    private String Data;

    public Node() {
        Id = UUID.randomUUID();
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public UUID getParent() {
        return Parent;
    }

    public void setParent(UUID parent) {
        Parent = parent;
    }

    public ArrayList<UUID> getChildren() {
        return Children;
    }

    public void setChildren(ArrayList<UUID> children) {
        Children = children;
    }

    public Integer getHeight() {
        return Height;
    }

    public void setHeight(Integer height) {
        Height = height;
    }

    public UUID getRoot() {
        return Root;
    }

    public void setRoot(UUID root) {
        Root = root;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
