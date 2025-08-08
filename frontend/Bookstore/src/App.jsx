import { useEffect, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import axios from 'axios';





function App() {
  const [userData, setUserData] = useState(null);
const [books, setBooks] = useState([]);

useEffect(() => {
    const username = "johndoe@nasdaq.us";
    const password = "garfield";
    const loginData = { username, password };

    axios.post("http://localhost:8080/login", loginData, { withCredentials: true })
    .then(response => {
        setUserData(response.data.customer);
        // Now fetch the books after successful login
        axios.get("http://localhost:8080/")
            .then(bookResponse => setBooks(bookResponse.data.books))
            .catch(err => console.error("Error fetching books:", err));
    })
    .catch(error => {
        console.error("Login failed:", error);
    });
}, []);
  



  return (
    <>
        {userData ? (
            <div>
                <h2>Welcome, {userData.firstname}</h2>
                <h3>Book List</h3>
                <ul>
                    {books.map(book => (
                        <li key={book.bookID}>{book.title} by {book.author.name}</li>
                    ))}
                </ul>
            </div>
        ) : (
            <p>Please log in...</p>
        )}
    </>
);
}

export default App
