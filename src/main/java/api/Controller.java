package api;

/*import data.Node;
import data.NodeRepository;
import data.Tree;
import data.TreeRepository;*/
import data.Node;
import data.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;
import service.NodeService;

import java.util.UUID;

@SpringBootApplication
@ComponentScan({"data"})
@EntityScan("data")
@EnableJpaRepositories("data")
@RestController
public class Controller {

    private NodeService nodeService;

    @GetMapping("/getNode/{nodeIdentifier}")
    public @ResponseBody Node getNode(@PathVariable UUID nodeIdentifier) {
        return nodeService.getNode(nodeIdentifier);
    }

    @PostMapping("/changeParent/{nodeId}/{newParentId}")
    public @ResponseBody boolean changeParent(@PathVariable UUID nodeId, @PathVariable UUID newParentId) {
        try {
            return nodeService.changeParent(nodeId, newParentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/addNode/{parentIdentifier}/{data}")
    public @ResponseBody Node addNode(@PathVariable UUID parentIdentifier, @PathVariable String data) {
        Node node = null;
        try {
            node = nodeService.addNode(data, parentIdentifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return node;
    }

    @GetMapping("/getAllNodes")
    public @ResponseBody Iterable<Node> getAllNodes()
    {
        return nodeService.getAllNodes();
    }

    @GetMapping("/getAllChildren/{nodeId}")
    public @ResponseBody Iterable<Node> getAllChildren(@PathVariable UUID nodeId)
    {
        try {
            return nodeService.getAllChildren(nodeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        SpringApplication.run(Controller.class, args);
    }

    @Bean
    public CommandLineRunner init(NodeRepository nodeRepository) {
        nodeService = new NodeService(nodeRepository);

        return (args) -> {
            Node myroot = nodeService.addNode("myroot", null);
            Node a = nodeService.addNode("a", myroot.getId());
            Node b = nodeService.addNode("b", myroot.getId());
            Node c = nodeService.addNode("c", a.getId());
            Node d = nodeService.addNode("d", a.getId());
        };
    }
}
