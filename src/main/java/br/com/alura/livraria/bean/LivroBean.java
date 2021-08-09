package br.com.alura.livraria.bean;


import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.alura.livraria.dao.DAO;
import br.com.alura.livraria.modelo.Autor;
import br.com.alura.livraria.modelo.Livro;

/**
 * @author Marcello
 *
 */
@ManagedBean 
//O ViewScoped mantém o estado do bean 
//enquanto houver requisições da mesma view/página
//Isso resolve o problema do autor não perder o relacionamento com o livro 
//assim que clicamos em gravar autor e depois gravar livro
@ViewScoped 
public class LivroBean {
	
	private Livro livro = new Livro();
	private Integer autorId;
	
	public Livro getLivro() {
		return livro;
	}
		
	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}
	
	/*Pega os autores que já receberam "Gravar Autor" */
	public List<Autor> getAutoresDoLivro(){
		return this.livro.getAutores();
	}

/*Esse método não grava no banco, somente associa o autor ao livro*/
/**/
public void gravarAutor() {
	Autor autor = new DAO<Autor>(Autor.class).buscaPorId(this.autorId); //busca na tabela pelo id
	this.livro.adicionaAutor(autor); //associa ao livro
	
	System.out.print("gravou" + autor.getNome());
}

public List<Livro> getLivros(){
	return new DAO<Livro>(Livro.class).listaTodos();
}



	/*Método que aproveita o DAO que lista todos autores do banco*/
	public List<Autor> getAutores(){
		return new DAO<Autor>(Autor.class).listaTodos();
	}
   
	public void gravar() {
        System.out.println("Gravando livro " + this.livro.getTitulo());
        
        if(livro.getAutores().isEmpty()) {
        	//throw new RuntimeException("Livro deve ter pelo menos um Autor");
        	FacesContext.getCurrentInstance().addMessage("autor", new FacesMessage("Livro deve ter pelo menos um Autor"));
        	return;
        }
        new DAO<Livro>(Livro.class).adiciona(this.livro);
        this.livro = new Livro();
    }
	
	//FacesContext -> Objeto que permite obter informações da view processada no momento
	//UiComponente -> Componente da view que está sendo validado
	//Object -> valor digitado pelo usuáro
	// A função precisa lançar validator exception(sinalizar que algo deu errado)
	public void comecaComDigitoUm(FacesContext fc, UIComponent ui, Object value) throws ValidatorException{
		String valor = value.toString();
		if(!valor.startsWith("1")){
			throw new ValidatorException(new FacesMessage("ISBN deveria começar com 1"));
		}
	}
	
	public String formAutor() {
		System.out.println("Chamando o formulario autor");
		return "autor?faces-redirect=true";  //Redirecionar também pelo navegador
		
	}

}

