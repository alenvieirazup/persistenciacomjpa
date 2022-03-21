package br.com.zup.lojajpa;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProdutoTest {

    private static EntityManagerFactory entityManagerFactory;
    private static final String PERSISTENCE_UNIT_NAME = "lojajpa";

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    @AfterAll
    public static void destroy() {
        entityManagerFactory.close();
    }

    @Test
    public void testCreateEntityManager() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        assertNotNull(entityManager);
    }

    @Test
    public void testPersistProduto() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Produto celular = new Produto();
        celular.setNome("Xiaomi Redmi");
        celular.setDescricao("Muito legal");
        celular.setPreco(new BigDecimal("800"));
        entityManager.persist(celular);
        entityManager.getTransaction().commit();
        assertNotNull(celular.getId());
    }
}