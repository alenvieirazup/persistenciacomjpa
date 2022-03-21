package br.com.zup.lojajpa;

import br.com.zup.lojajpa.dao.CategoriaDao;
import br.com.zup.lojajpa.dao.ProdutoDao;
import br.com.zup.lojajpa.modelo.Categoria;
import br.com.zup.lojajpa.modelo.Produto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
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
    public void testCriarEntityManager() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        assertNotNull(entityManager);
    }

    @Test
    public void testPersistirProduto() {
        Produto celular = new Produto();
        celular.setNome("Xiaomi Redmi");
        celular.setDescricao("Muito legal");
        celular.setPreco(new BigDecimal("800"));
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(celular);
        entityManager.getTransaction().commit();
        assertNotNull(celular.getId());
    }

    @Test
    public void testPersistirProdutoComCategoria() {
        Categoria celulares = new Categoria("CELULARES");
        Produto celular = new Produto("Xiaomi Redmi", "Muito legal", new BigDecimal("800"), celulares );

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ProdutoDao produtoDao = new ProdutoDao(entityManager);
        CategoriaDao categoriaDao = new CategoriaDao(entityManager);

        entityManager.getTransaction().begin();

        categoriaDao.cadastrar(celulares);
        produtoDao.cadastrar(celular);

        entityManager.getTransaction().commit();
        entityManager.close();

        assertAll(() -> assertNotNull(celular.getId()),
                () -> assertNotNull(celulares.getId()));
    }
}