insert into authors(id, first_name, last_name, description)
values (1, 'John', 'Doe', 'Description'),
(2, 'Anton', 'Surname', 'Desciption');
alter table authors alter COLUMN id RESTART with 3;

insert into books(id, title, description, genre, pages_count, author_id)
values (1, 'Title', 'Description', 'FICTION', 123, 1),
(2, 'Book', 'Book description', 'FICTION', 94, 1);
alter table books alter COLUMN id RESTART with 3;

insert into borrowed_books(book_id, user_id, borrow_date, return_date)
values (1, 1, '2024-12-23 13:21:54.819000 +00:00', '2024-12-23 13:21:54.819000 +00:00')