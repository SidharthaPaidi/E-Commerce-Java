import { useState } from "react";
import axios from "axios";

const SearchBar = () => {
  const [input, setInput] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [noResults, setNoResults] = useState(false);
  const [showSearchResults, setShowSearchResults] = useState(false);

  const handleChange = async (value) => {
    setInput(value);
    if (value.length >= 1) {
      setShowSearchResults(true);
      try {
        const response = await axios.get(
          `http://localhost:8080/api/products/search?keyword=${value}`
        );
        setSearchResults(response.data);
        setNoResults(response.data.length === 0);
      } catch (error) {
        console.error("Error searching:", error);
      }
    } else {
      setShowSearchResults(false);
      setSearchResults([]);
      setNoResults(false);
    }
  };

  return (
    <div className="search-bar-container position-relative" style={{ width: '250px' }}>
      <input
        className="form-control me-2"
        type="search"
        placeholder="Search products..."
        aria-label="Search"
        value={input}
        autoComplete="off"
        style={{ borderRadius: '20px', paddingLeft: '16px' }}
        onChange={(e) => handleChange(e.target.value)}
      />
      {showSearchResults && (
        <div
          className="suggestions-dropdown shadow"
          style={{
            position: 'absolute',
            top: '110%',
            left: 0,
            right: 0,
            zIndex: 1050,
            background: '#fff',
            borderRadius: '10px',
            maxHeight: '260px',
            overflowY: 'auto',
            boxShadow: '0 4px 16px rgba(0,0,0,0.10)',
          }}
        >
          {searchResults.length > 0 ? (
            <ul className="list-group list-group-flush">
              {searchResults.map((result) => (
                <li
                  key={result.id}
                  className="list-group-item suggestion-item"
                  style={{ cursor: 'pointer', border: 'none', padding: '10px 16px' }}
                  onMouseDown={() => window.location.href = `/product/${result.id}`}
                  onMouseOver={e => e.currentTarget.style.background = '#f5f5f5'}
                  onMouseOut={e => e.currentTarget.style.background = '#fff'}
                >
                  <span style={{ fontWeight: 500 }}>{result.name}</span>
                </li>
              ))}
            </ul>
          ) : (
            noResults && (
              <div className="no-results-message text-center text-muted py-2" style={{ fontSize: '0.95rem' }}>
                <i className="bi bi-emoji-frown" style={{ marginRight: 6 }}></i>
                No product found
              </div>
            )
          )}
        </div>
      )}
    </div>
  );
};

export default SearchBar;
