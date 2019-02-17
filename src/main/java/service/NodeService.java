package service;

import data.Node;
import data.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NodeService {

    private NodeRepository nodeRepository;

    private Node root;

    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public boolean changeParent(UUID nodeId, UUID newParentId) throws Exception {
        Optional<Node> node = nodeRepository.findById(nodeId);
        if (!node.isPresent()) {
            throw new Exception("node doesn't exist");
        }
        Optional<Node> newParent = nodeRepository.findById(newParentId);
        Optional<Node> oldParent = nodeRepository.findById(node.get().getParent());
        if (newParent.isPresent()) {
            if (node.get().getParent() == null) { // root node
                throw new Exception("cannot change root node");
            }
            oldParent.get().getChildren().remove(node.get().getId());
            nodeRepository.save(oldParent.get());
            newParent.get().getChildren().add(node.get().getId());
            nodeRepository.save(newParent.get());
            node.get().setParent(newParentId);
            nodeRepository.save(node.get());
            updateHeight(node.get());
            return true;
        } else {
            throw new Exception("parent doesn't exist");
        }
    }

    private void updateHeight(Node parent) {
        parent.setHeight(calculateHeight(parent));
        nodeRepository.save(parent);
        parent.getChildren().forEach(childId -> {
            Optional<Node> child = nodeRepository.findById(childId);
            child.ifPresent(this::updateHeight);
        });
    }

    public Node getNode(UUID id) {
        Optional<Node> node = nodeRepository.findById(id);
        return node.orElse(null);
    }

    public Iterable<Node> getAllNodes() {
        return nodeRepository.findAll();
    }

    public Iterable<Node> getAllChildren(UUID nodeId) throws Exception {
        List<Node> children = new ArrayList<>();
        Optional<Node> parent = nodeRepository.findById(nodeId);
        if (!parent.isPresent()) {
            throw new Exception("parent doesn't exist");
        }
        parent.get().getChildren().forEach(childId -> {
            Optional<Node> child = nodeRepository.findById(childId);
            child.ifPresent(children::add);
        });
        return children;
    }

    public Node addNode(String data, UUID parentIdentifier) throws Exception {
        Node node = new Node();

        if (parentIdentifier == null) { //root node
            if (root != null) {
                throw new Exception("root already exist");
            }
            root = node;
            node.setHeight(0);
        }
        else {
            Optional<Node> parent = nodeRepository.findById(parentIdentifier);
            if (!parent.isPresent()) {
                throw new Exception("parent doesn't exist");
            }
            parent.get().getChildren().add(node.getId());
            nodeRepository.save(parent.get());
            node.setParent(parentIdentifier);
            node.setHeight(calculateHeight(node));
        }
        node.setRoot(root.getId());
        node.setData(data);

        nodeRepository.save(node);
        return node;
    }

    private int calculateHeight(Node node) {
        int height = 0;
        Node current = node;
        while (current.getParent() != null) {
            current = nodeRepository.findById(current.getParent()).get();
            height++;
        }
        return height;
    }
}
