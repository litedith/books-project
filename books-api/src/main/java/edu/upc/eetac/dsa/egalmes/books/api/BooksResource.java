package edu.upc.eetac.dsa.egalmes.books.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.egalmes.books.api.DataSourceSPA;
import edu.upc.eetac.dsa.egalmes.books.api.MediaType;
import edu.upc.eetac.dsa.egalmes.books.api.model.BookCollection;
import edu.upc.eetac.dsa.egalmes.books.api.model.Review;
import edu.upc.eetac.dsa.egalmes.books.api.model.User;
import edu.upc.eetac.dsa.egalmes.books.api.model.Book;

@Path("/books")
public class BooksResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	@Context
	SecurityContext security;

	private boolean administrator;
	private boolean registered;

	@GET
	@Produces(MediaType.BOOK_API_BOOK_COLLECTION)
	public BookCollection getBooks(@QueryParam("length") int length,
			@QueryParam("after") int after) {
		
		setAdministrator(security.isUserInRole("admin"));
		BookCollection books = new BookCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();// Conectamos con la base de datos
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;

		try {
			boolean updateFromLast = after > 0;
			stmt = conn.prepareStatement(buildGetBooksQuery(updateFromLast));
			
			if (updateFromLast) {
				if (length == 0) {
					stmt.setInt(1, after);
					stmt.setInt(2, 5);
				} else {
					stmt.setInt(1, after);
					stmt.setInt(2, length);
				}
			} else {

				if (length == 0)
					stmt.setInt(1, 5);
				else
					stmt.setInt(1, length);
			}

			ResultSet rs = stmt.executeQuery();
		
			while (rs.next()) {
				Book book = new Book();

				book.setIdbook(rs.getString("idbook"));
				book.setTittle(rs.getString("tittle"));
				book.setAuthor(rs.getString("author"));
				book.setLanguage(rs.getString("language"));
				book.setEdition(rs.getString("edition"));
				book.setDateedition(rs.getDate("dateedition"));
				book.setDateprint(rs.getDate("dateprint"));
				book.setEditorial(rs.getString("editorial"));

				PreparedStatement stmt1 = null;
				
				stmt1 = conn.prepareStatement(buildGetReviewBookByIdQuery());
				stmt1.setString(1, book.getIdbook());
				
				ResultSet rs2 = stmt1.executeQuery();
				
				while (rs2.next()) {
					Review review = new Review();
					review.setIdbook(rs2.getString("idbook"));
					review.setUsername(rs2.getString("username"));
					review.setText(rs2.getString("text"));
					review.setDateupdate(rs2.getString("dateupdate"));
					review.setIdreview(rs2.getString("idreview"));

					book.addReview(review);
					
				}
				
				books.addBook(book);// Añadimos toda la info dellibro (
									// inclusive sus reviews ).
			

			}
			

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return books;
	}

	private String buildGetReviewBookByIdQuery() {
		return "select * from review  where idbook=?;";
	}

	private String buildGetBooksQuery(boolean updateFromLast) {

		if (updateFromLast)
			return "select * from book where idbook>? limit ?;";
		else
			return "select *from book limit ?;";
	}

	@GET
	@Path("/search")
	@Produces(MediaType.BOOK_API_BOOK_COLLECTION)
	public BookCollection getBooksFromAuthorTittle(
			@QueryParam("author") String author,
			@QueryParam("tittle") String tittle,
			@QueryParam("length") int length) {

		BookCollection books = new BookCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();// Conectamos con la base de datos
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(buildGetBooksAuthorTittleQuery(author,
					tittle));
			if (author != null && tittle == null) {
				stmt.setString(1, "%" + author + "%");
				if (length == 0) {
					length = 3;
					stmt.setInt(2, length);
				} else {
					stmt.setInt(2, length);
				}
			}
			if (author == null && tittle != null) {
				stmt.setString(1, "%" + tittle + "%");
				if (length == 0) {
					length = 3;
					stmt.setInt(2, length);
				} else {
					stmt.setInt(2, length);
				}
			}

			if (author != null && tittle != null) {
				stmt.setString(2, "%" + author + "%");
				stmt.setString(1, "%" + tittle + "%");
				if (length == 0) {
					length = 3;
					stmt.setInt(3, length);
				} else {
					stmt.setInt(3, length);
				}
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Book book = new Book();

				book.setIdbook(rs.getString("idbook"));
				book.setTittle(rs.getString("tittle"));
				book.setAuthor(rs.getString("author"));
				book.setLanguage(rs.getString("language"));
				book.setEdition(rs.getString("edition"));
				book.setDateedition(rs.getDate("dateedition"));
				book.setDateprint(rs.getDate("dateprint"));
				book.setEditorial(rs.getString("editorial"));

				PreparedStatement stmt1 = null;
				stmt1 = conn.prepareStatement(buildGetReviewBookByIdQuery());
				stmt1.setString(1, book.getIdbook());

				ResultSet rs2 = stmt1.executeQuery();

				while (rs2.next()) {
					Review review = new Review();
					review.setIdbook(rs2.getString("idbook"));
					review.setUsername(rs2.getString("username"));
					review.setText(rs2.getString("text"));
					review.setDateupdate(rs2.getString("dateupdate"));
					review.setIdreview(rs2.getString("idreview"));

					book.addReview(review);

				}

				books.addBook(book);

			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return books;
	}

	private String buildGetBooksAuthorTittleQuery(String a, String t) {
		if (a == null & t == null) {
			throw new BadRequestException("Subject and Content can't be null. ");
		}

		if (a == null & t != null) {

			return "select * from book where tittle like ? limit ? ";
		}
		if (a != null & t == null) {
			return "select * from book where author like ? limit ? ";
		}
		if (a != null & t != null) {
			return "select * from book where tittle like ? and author like ? limit ?";
		}

		return null;

	}

	@GET
	@Path("/searching")
	@Produces(MediaType.BOOK_API_BOOK_COLLECTION)
	public BookCollection getBooksFromAuthor(@QueryParam("author") String author) {

		BookCollection books = new BookCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();// Conectamos con la base de datos
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(buildGetBooksAuthorTittleQuery(author));
			

			stmt.setString(1, "%" + author + "%");
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Book book = new Book();

				book.setIdbook(rs.getString("idbook"));
				book.setTittle(rs.getString("tittle"));
				book.setAuthor(rs.getString("author"));
				book.setLanguage(rs.getString("language"));
				book.setEdition(rs.getString("edition"));
				book.setDateedition(rs.getDate("dateedition"));
				book.setDateprint(rs.getDate("dateprint"));
				book.setEditorial(rs.getString("editorial"));

				PreparedStatement stmt1 = null;
				stmt1 = conn.prepareStatement(buildGetReviewBookByIdQuery());
				stmt1.setString(1, book.getIdbook());

				ResultSet rs2 = stmt1.executeQuery();

				while (rs2.next()) {
					Review review = new Review();
					review.setIdbook(rs2.getString("idbook"));
					review.setUsername(rs2.getString("username"));
					review.setText(rs2.getString("text"));
					review.setDateupdate(rs2.getString("dateupdate"));
					review.setIdreview(rs2.getString("idreview"));

					book.addReview(review);

				}

				books.addBook(book);

			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return books;
	}

	private String buildGetBooksAuthorTittleQuery(String a) {
		
		return "select * from book where author like ? ";
	}
	
	
	@GET
	@Path("/{idbook}")
	@Produces(MediaType.BOOK_API_BOOK)
	public Response getBook(@PathParam("idbook") String idbook,
			@Context Request request) {
		CacheControl cc = new CacheControl();
		Book book = getBookFromDatabase(idbook);

		Review review = new Review();
		// Calculate the ETag on last modified date of user resource lo crea con
		// el lastmodified
		String s = book.getAuthor() + " " + book.getIdbook() + " "
				+ review.getDateupdate();

		EntityTag eTag = new EntityTag(Long.toString(s.hashCode()));
		// EntityTag eTag = new
		// EntityTag(Long.toString(book.getReviews().hashCode()));
		// Verify if it matched with etag available in http request
		// mira si coincide con el etag
		Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

		// If ETag matches the rb will be non-null;
		// Use the rb to return the response without any further processing
		if (rb != null) {
			return rb.cacheControl(cc).tag(eTag).build();
		}

		// If rb is null then either it is first time request; or resource is
		// modified
		// Get the updated representation and return with Etag attached to it
		rb = Response.ok(book).cacheControl(cc).tag(eTag);

		return rb.build();

	}

	private Book getBookFromDatabase(String idbook) {
		Book book = new Book();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {

			System.out.println("Antes de preparar query");
			stmt = conn.prepareStatement(buildGetBookByIdQuery());
			stmt.setInt(1, Integer.valueOf(idbook));
			ResultSet rs = stmt.executeQuery();
			System.out.println("ejecutando query");
			if (rs.next()) {
				book.setIdbook(rs.getString("idbook"));
				book.setTittle(rs.getString("tittle"));
				book.setAuthor(rs.getString("author"));
				book.setLanguage(rs.getString("language"));
				book.setEdition(rs.getString("edition"));
				book.setDateedition(rs.getDate("dateedition"));
				book.setDateprint(rs.getDate("dateprint"));
				book.setEditorial(rs.getString("editorial"));
				System.out.println("poniendo parametros query");
			} else {
				throw new NotFoundException("There's no book with id=" + idbook);
			}
			stmt.close();
			stmt = conn.prepareStatement(buildGetReviewByIdQuery());
			stmt.setInt(1, Integer.valueOf(idbook));
			rs = stmt.executeQuery();
			System.out.println("ejecutando segunda query");
			while (rs.next()) {

				Review review1 = new Review();
				review1.setIdbook(rs.getString("idbook"));
				review1.setUsername(rs.getString("username"));
				review1.setText(rs.getString("text"));
				review1.setDateupdate(rs.getString("dateupdate"));
				review1.setIdreview(rs.getString("idreview"));
				System.out.println("parametros query2");
				book.addReview(review1);
				System.out.println("Añadiendo review al libro");

			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return book;
	}

	private Review getReviewFromDatabase(String idreview) {

		Review review1 = new Review();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt1 = null;
		try {
			stmt1 = conn.prepareStatement(buildGetReviewByIdQuery());
			stmt1.setInt(1, Integer.valueOf(idreview));
			ResultSet rs = stmt1.executeQuery();

			if (rs.next()) {

				review1.setIdbook(rs.getString("idbook"));
				review1.setUsername(rs.getString("username"));
				review1.setText(rs.getString("text"));
				review1.setDateupdate(rs.getString("dateupdate"));
				review1.setIdreview(rs.getString("idreview"));

			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt1 != null)
					stmt1.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return review1;
	}

	private String buildGetBookByIdQuery() {
		return "select * from book where idbook=?;";
	}

	private String buildGetReviewByIdQuery() {
		return "select * from review where idreview = ?";
	}

	@POST
	@Consumes(MediaType.BOOK_API_BOOK)
	@Produces(MediaType.BOOK_API_BOOK)
	public Book createBook(Book book) {
		if (!security.isUserInRole("admin"))
			throw new ForbiddenException("You are not allowed to create a book");
		setAdministrator(security.isUserInRole("admin"));
		validateBook(book);
		System.out.println("1");

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildInsertBook();// query
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);// te
																				// vuelve
																				// la
																				// clave
																				// primaria
																				// que
																				// se
																				// ha
																				// generado
			// parametrizados 1,2,3
			// el auto no hay que pasarlo, esta en la tabla de usuarios, ni id
			// ni lastmod pq son autogenerados
			// stmt.setString(1, sting.getUsername());
			stmt.setString(2, book.getAuthor());
			stmt.setString(1, book.getTittle());
			stmt.setString(3, book.getLanguage());
			stmt.setString(4, book.getEdition());
			stmt.setDate(6, book.getDateprint());
			stmt.setDate(5, book.getDateedition());
			stmt.setString(7, book.getEditorial());

			stmt.executeUpdate();// consulta
			System.out.println("2");
			ResultSet rs = stmt.getGeneratedKeys();// recojo respuesta
			if (rs.next()) {
				int idbook = rs.getInt(1);

				book = getBookFromDatabase(Integer.toString(idbook));
			} else {
				// Something has failed...
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return book;
	}

	@PUT
	@Path("/{idbook}")
	@Consumes(MediaType.BOOK_API_BOOK)
	@Produces(MediaType.BOOK_API_BOOK)
	public Book updateBook(@PathParam("idbook") String idbook, Book book) {
		if (!security.isUserInRole("admin"))
			throw new ForbiddenException("You are not allowed to update a book");
		
		setAdministrator(security.isUserInRole("admin"));

		validateUpdateBook(book);
		System.out.println("secua");
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			System.out.println("secua1");
			String sql = buildUpdateBook();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, book.getTittle());
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getLanguage());
			stmt.setString(4, book.getEdition());
			stmt.setDate(5, book.getDateedition());
			stmt.setDate(6, book.getDateprint());
			stmt.setString(7, book.getEditorial());
			stmt.setInt(8, Integer.valueOf(idbook));
			System.out.println("1a");
			int rows = stmt.executeUpdate();
			if (rows == 1)
				book = getBookFromDatabase(idbook);
			else {
				throw new NotFoundException("There's no book with thi id="
						+ idbook);
				// Updating inexistent sting
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return book;
	}

	private String buildUpdateBook() {

		return "update book set tittle=ifnull(?, tittle), author=ifnull(?, author), language=ifnull(?, language), edition=ifnull(?, edition), dateedition=ifnull(?, dateedition), dateprint=ifnull(?, dateprint), editorial=ifnull(?, editorial) where idbook=?;";
	}

	private void validateUpdateBook(Book book) {
		if (book.getEditorial() != null && book.getEditorial().length() > 20)
			throw new BadRequestException(
					"editorial can't be greater than 50 characters.");

		if (book.getAuthor() != null && book.getAuthor().length() > 50)
			throw new BadRequestException(
					"author can't be greater than 50 characters.");

		if (book.getTittle() != null && book.getTittle().length() > 50)
			throw new BadRequestException(
					"tittle can't be greater than 50 characters.");
		if (book.getLanguage() != null && book.getLanguage().length() > 20)
			throw new BadRequestException(
					"language can't be greater than 20 characters.");
		if (book.getEdition() != null && book.getEdition().length() > 20)
			throw new BadRequestException(
					"Edition can't be greater than 20 characters.");
	}

	private String buildInsertBook() {
		return "insert into book (tittle, author, language, edition, dateedition, dateprint, editorial) values (?, ?, ? , ?, ?, ?, ?);";

	}

	private void validateBook(Book book) {
		if (book.getTittle() == null)
			throw new BadRequestException("Tittle can't be null.");
		if (book.getLanguage() == null)
			throw new BadRequestException("Language can't be null.");
		if (book.getAuthor() == null)
			throw new BadRequestException("author can't be null.");

		if (book.getAuthor().length() > 50)
			throw new BadRequestException(
					"author can't be greater than 50 characters.");

		if (book.getTittle().length() > 50)
			throw new BadRequestException(
					"tittle can't be greater than 50 characters.");
		if (book.getLanguage().length() > 20)
			throw new BadRequestException(
					"language can't be greater than 20 characters.");
	}

	@DELETE
	@Path("/{idbook}")
	public void deleteBook(@PathParam("idbook") String idbook) {
		if (!security.isUserInRole("admin"))
			throw new ForbiddenException("You are not allowed to delete a book");
		
		setAdministrator(security.isUserInRole("admin"));

		// validateUser(idbook);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			String sql = buildDeleteBook();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.valueOf(idbook));

			int rows = stmt.executeUpdate();
			if (rows == 0)// te has intentado cargar algo que no existia
				throw new NotFoundException("There's no book with id=" + idbook);// Deleting
																					// inexistent
																					// sting
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {// cierro conexiones
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	private String buildDeleteBook() {
		return "delete from book where idbook= ? ;";
		// return
		// "delete b.*, r.* from book b, review r where b.idbook = r.idbook and b.idbook=?;";
	}

	@POST
	@Path("/{bookid}/reviews")
	@Consumes(MediaType.BOOK_API_REVIEW)
	@Produces(MediaType.BOOK_API_BOOK)
	public Book createReview(@PathParam("bookid") String bookid, Review review) {
		if (!security.isUserInRole("registered"))
			throw new ForbiddenException(
					"You are not allowed to create reviews for a book");
		
		setRegistered(security.isUserInRole("registered"));
		
		Book book = new Book();
		validateReview(review);

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {

			stmt = conn.prepareStatement(buildValidateUserReview());
			stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setInt(2, Integer.valueOf(bookid));
			ResultSet rs = stmt.executeQuery();
			if (rs == null) {
				stmt.close();
				String sql = buildInsertReview();// query
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, Integer.parseInt(bookid));
				stmt.setString(2, security.getUserPrincipal().getName());
				stmt.setString(3, review.getText());

				stmt.executeUpdate();// consulta

				book = getBookFromDatabase(bookid);
			} else {
				throw new NotFoundException(
						"ya esxiste una review de este libro con tu username");
				// Updating inexistent sting
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return book;
	}

	private void validateReview(Review review) {

		// VALIDAR QUE EL USUARIO NO TIENE UNA REVIEW

		if (review.getText() == null)
			throw new BadRequestException("TExt can't be null.");

		if (review.getText().length() > 500)
			throw new BadRequestException(
					"Text can't be greater than 500 characters.");
	}

	private String buildValidateUserReview() {
		return "select * from review where username = ? AND idbook = ? ";
	}

	private String buildInsertReview() {
		return "insert into review (idbook, username, text) value (?,?,?);";
	}

	@PUT
	@Path("/{idbook}/reviews/{idreview}")
	@Consumes(MediaType.BOOK_API_REVIEW)
	@Produces(MediaType.BOOK_API_REVIEW)
	public Review updateReview(@PathParam("idbook") String idbook,
			@PathParam("idreview") String idreview, Review review) {
		if (!security.isUserInRole("registered"))
			throw new ForbiddenException(
					"You are not allowed to create reviews for a book");
		setRegistered(security.isUserInRole("registered"));

		// validateUser(idbook);
		validateUpdateReview(review, idreview);

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildUpdateReview();
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, review.getText());
			System.out.println(review.getText());
			stmt.setInt(2, Integer.valueOf(idreview));
			// System.out.println( Integer.valueOf(idbook));
			// stmt.setString(3, security.getUserPrincipal().getName());
			// System.out.println(security.getUserPrincipal().getName());

			int rows = stmt.executeUpdate();

			if (rows == 1) {
				review = getReviewFromDatabase(idreview);
				System.out.println(review);
			}

			else {
				throw new NotFoundException(
						"There's no review for book with this id=" + idbook);
				// Updating inexistent sting
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return review;
	}

	private void validateUser(String idbook) {
		Review currentbook = getReviewFromDatabase(idbook);
		if (!security.getUserPrincipal().getName()
				.equals(currentbook.getUsername()))
			throw new ForbiddenException(
					"You are not allowed to modify this book.");
	}

	private void validateUpdateReview(Review review, String idreview) {
		if (review.getText().length() > 500)
			throw new BadRequestException(
					"Text can't be greater than 100 characters.");
		Review rev = getReviewFromDatabase(idreview);
				if (!security.getUserPrincipal().getName().equals(rev.getUsername()))
				throw new ForbiddenException("You're not allowed to modify this review");
	}

	private String buildUpdateReview() {
		return "update review set text=ifnull(?, text) where idreview = ?;";
	}

	@DELETE
	@Path("/{idbook}/reviews/{idreview}")
	public void deleteReview(@PathParam("idbook") String idbook,
			@PathParam("idreview") String idreview) {
		if (security.isUserInRole("registered")) {
			if (!security.isUserInRole("registered"))
				throw new ForbiddenException(
						"You are not allowed to create reviews for a book");
			
			setRegistered(security.isUserInRole("registered"));

			// validateUser(idbook);
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException(
						"Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
			PreparedStatement stmt = null;
			try {
				String sql = buildDeleteReview();
				stmt = conn.prepareStatement(sql);
				// stmt.setInt(1, Integer.valueOf(idbook));
				stmt.setInt(1, Integer.valueOf(idreview));
				int rows = stmt.executeUpdate();
				if (rows == 0)// te has intentado cargar algo que no existia
					throw new NotFoundException("There's no book with idbook="
							+ idbook);// Deleting inexistent sting
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {// cierro conexiones
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

		if (security.isUserInRole("admin")) {
			
			if (!security.isUserInRole("admin"))
				throw new ForbiddenException(
						"You are not allowed to create reviews for a book");
			setAdministrator(security.isUserInRole("admin"));
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException(
						"Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
			PreparedStatement stmt = null;
			try {
				String sql = buildDeleteReview();
				stmt = conn.prepareStatement(sql);
				// stmt.setInt(1, Integer.valueOf(idbook));
				stmt.setInt(1, Integer.valueOf(idreview));
				int rows = stmt.executeUpdate();
				if (rows == 0)// te has intentado cargar algo que no existia
					throw new NotFoundException("There's no book with idbook="
							+ idbook);// Deleting inexistent sting
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {// cierro conexiones
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
				}
			}

		}
	}

	private String buildDeleteReview() {
		return "delete from review where idreview = ?;";
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	@GET
	@Produces(MediaType.BOOK_API_REVIEW)
	@Path("/{idbook}/reviews/{idreview}")
	public Review getReview(@PathParam("idbook") String idbook,
			@PathParam("idreview") String idreview) {
		Review r = new Review();

		r.setIdbook(idbook);
		r.setIdreview(idreview);
		return r;
	}

	@GET
	@Path("/meloinvento")
	public String getMeloinvento() {
		return "me lo invento";
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

}
