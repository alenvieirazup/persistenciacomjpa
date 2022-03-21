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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    private static final String PERSISTENCE_UNIT_NAME = "lojajpa";
    private static EntityManagerFactory entityManagerFactory;

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
        entityManager.close();
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
        entityManager.close();
        assertNotNull(celular.getId());
    }

    @Test
    public void testPersistirProdutoComCategoria() {
        Categoria celulares = new Categoria("CELULARES");
        Produto celular = new Produto("Xiaomi Redmi", "Muito legal", new BigDecimal("800"), celulares);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ProdutoDao produtoDao = new ProdutoDao(entityManager);
        CategoriaDao categoriaDao = new CategoriaDao(entityManager);

        entityManager.getTransaction().begin();

        categoriaDao.cadastrar(celulares);
        produtoDao.cadastrar(celular);

        entityManager.getTransaction().commit();
        entityManager.close();

        assertAll(() -> assertNotNull(celular.getId()), () -> assertNotNull(celulares.getId()));
    }

    @Test
    public void testPersistirProdutoAtualizarNaoAtualizarDepoisTransacaoLimpada() {
        Produto celular = new Produto("Produto 1", "Bom", new BigDecimal("500"));

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ProdutoDao produtoDao = new ProdutoDao(entityManager);

        entityManager.getTransaction().begin();

        produtoDao.cadastrar(celular);

        celular.setPreco(new BigDecimal(200));
        entityManager.flush();
        entityManager.clear();

        celular.setPreco(new BigDecimal(100));
        entityManager.flush();
        Produto produto = produtoDao.buscarPorId(celular.getId());
        entityManager.getTransaction().commit();
        entityManager.close();
        assertAll(() -> assertNotNull(celular.getId()), () -> assertTrue(produto.getPreco().compareTo(new BigDecimal(200.0)) == 0));
    }

    @Test
    public void testPersistirProdutoEAtualizar() {
        Produto celular = new Produto("Produto 2", "Ruim", new BigDecimal("500"));

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ProdutoDao produtoDao = new ProdutoDao(entityManager);

        entityManager.getTransaction().begin();

        produtoDao.cadastrar(celular);
        entityManager.flush();
        entityManager.clear();
        celular.setPreco(new BigDecimal(200));
        produtoDao.atualizar(celular);
        entityManager.getTransaction().commit();
        entityManager.close();
        assertAll(() -> assertNotNull(celular.getId()), () -> assertTrue(celular.getPreco().compareTo(new BigDecimal(200.0)) == 0));
    }

    @Test
    public void testPersistirProdutoERemover() {
        Produto celular = new Produto("Produto Top", "Show", new BigDecimal("1500"));

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ProdutoDao produtoDao = new ProdutoDao(entityManager);

        entityManager.getTransaction().begin();

        produtoDao.cadastrar(celular);
        entityManager.flush();
        entityManager.clear();

        produtoDao.remover(celular);
        Produto produto = produtoDao.buscarPorId(celular.getId());
        entityManager.getTransaction().commit();
        entityManager.close();
        assertNull(produto);
    }

    @Test
    public void testPersistirProdutoEBuscarPorNome() {
        Produto celular = new Produto("Tijolo", "Antigo", new BigDecimal("1"));

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ProdutoDao produtoDao = new ProdutoDao(entityManager);

        entityManager.getTransaction().begin();

        produtoDao.cadastrar(celular);

        entityManager.getTransaction().commit();
        List<Produto> produtos = produtoDao.buscarPorNome("Tijolo");
        entityManager.close();

        assertFalse(produtos.isEmpty());
    }

    @Test
    public void testPersistirProdutoEBuscarPorNomeDaCategoria() {
        Categoria celulares = new Categoria("C");
        Produto celular = new Produto("Tijolinho", "Novo", new BigDecimal("2"), celulares);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ProdutoDao produtoDao = new ProdutoDao(entityManager);
        CategoriaDao categoriaDao = new CategoriaDao(entityManager);

        entityManager.getTransaction().begin();

        categoriaDao.cadastrar(celulares);
        produtoDao.cadastrar(celular);

        entityManager.getTransaction().commit();

        List<Produto> produtos = produtoDao.buscarPorNomeDaCategoria("C");
        entityManager.close();

        assertFalse(produtos.isEmpty());
    }

}