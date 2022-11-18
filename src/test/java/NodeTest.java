import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class NodeTest {
	@Test
	public void addNewInBetween() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		// ()
		{
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			Node a = node("a");
			Node b = node("b");
			bind(a, b);
			entityManager.persist(a);
			transaction.commit();
		}
		// (a (b))
		{
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			Node a = entityManager.find(Node.class, "a");
			Node b = entityManager.find(Node.class, "b");
			unbind(a, b);
			Node x = node("x");
			bind(a, x);
			bind(x, b);
			transaction.commit();
		}
		// (a (x (b)))
		{
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			Node a = entityManager.find(Node.class, "a");
			Node b = entityManager.find(Node.class, "b");
			Node x = entityManager.find(Node.class, "x");
			assertNotNull(a);
			assertNotNull(b);
			assertNotNull(x);
			transaction.commit();
		}
	}
	
	private Node node(String id) {
		Node node = new Node();
		node.setId(id);
		node.setParent(null);
		node.setChildren(new HashSet<>());
		return node;
	}
	
	private void bind(Node parent, Node child) {
		parent.getChildren().add(child);
		child.setParent(parent);
	}
	
	private void unbind(Node parent, Node child) {
		parent.getChildren().remove(child);
		child.setParent(null);
	}
}
