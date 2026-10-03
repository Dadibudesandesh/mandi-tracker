
import { useEffect, useMemo, useState } from "react";
import "./App.css";

const API_BASE = "http://localhost:8080/api/v1";

function formatPrice(price) {
  if (price == null) return "—";

  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 2,
  }).format(price);
}

function App() {
  const [crops, setCrops] = useState([]);
  const [prices, setPrices] = useState([]);
  const [cropId, setCropId] = useState("");
  const [district, setDistrict] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadCrops() {
      try {
        const response = await fetch(`${API_BASE}/crops`);

        if (!response.ok) {
          throw new Error("Unable to load crops.");
        }

        const data = await response.json();
        setCrops(data);
      } catch (err) {
        setError(err.message || "Failed to load crops.");
      }
    }

    loadCrops();
  }, []);

  useEffect(() => {
    async function loadPrices() {
      setLoading(true);
      setError("");

      try {
        const params = new URLSearchParams();

        if (cropId) params.set("cropId", cropId);
        if (district.trim()) params.set("district", district.trim());

        const query = params.toString();
        const url = `${API_BASE}/prices${query ? `?${query}` : ""}`;

        const response = await fetch(url);

        if (!response.ok) {
          throw new Error(`Prices API failed: HTTP ${response.status}`);
        }

        const data = await response.json();
        setPrices(data);
      } catch (err) {
        setError(err.message || "Unable to load prices.");
        setPrices([]);
      } finally {
        setLoading(false);
      }
    }

    loadPrices();
  }, [cropId, district]);

  const districts = useMemo(() => {
    return [
      ...new Set(
        prices
          .map((item) => item.mandi?.district)
          .filter(Boolean)
      ),
    ].sort();
  }, [prices]);

  return (
    <main className="app-container">
      <header className="page-header">
        <p className="eyebrow">MAHARASHTRA AGRICULTURE</p>
        <h1>Mandi Tracker</h1>
        <p className="subtitle">
          बाजारभाव माहिती — Market prices at your fingertips
        </p>
      </header>

      <section className="section">
        <h2>Available Crops / उपलब्ध पिके</h2>

        {crops.length === 0 ? (
          <p>No crop records found.</p>
        ) : (
          <div className="crop-grid">
            {crops.map((crop) => (
              <button
                type="button"
                className={`crop-card ${
                  String(crop.id) === cropId ? "selected" : ""
                }`}
                key={crop.id}
                onClick={() =>
                  setCropId(
                    String(crop.id) === cropId ? "" : String(crop.id)
                  )
                }
              >
                <h3>{crop.name}</h3>
                <p className="marathi-name">{crop.nameMr}</p>
                <span>{crop.category}</span>
                <small>{crop.unit}</small>
              </button>
            ))}
          </div>
        )}
      </section>

      <section className="section">
        <div className="prices-heading">
          <div>
            <h2>Mandi Prices / बाजारभाव</h2>
            <p className="subtitle">
              Prices are displayed per quintal unless otherwise specified.
            </p>
          </div>

          <button
            className="clear-button"
            onClick={() => {
              setCropId("");
              setDistrict("");
            }}
          >
            Clear filters
          </button>
        </div>

        <div className="filters">
          <label>
            Crop / पीक
            <select
              value={cropId}
              onChange={(event) => setCropId(event.target.value)}
            >
              <option value="">All crops</option>
              {crops.map((crop) => (
                <option key={crop.id} value={crop.id}>
                  {crop.name} ({crop.nameMr})
                </option>
              ))}
            </select>
          </label>

          <label>
            District / जिल्हा
            <input
              type="text"
              placeholder="e.g. Sangli"
              value={district}
              onChange={(event) => setDistrict(event.target.value)}
            />
          </label>
        </div>

        {error && <div className="error-message">{error}</div>}

        {loading ? (
          <p className="status-message">Loading market prices...</p>
        ) : prices.length === 0 ? (
          <div className="empty-state">
            <h3>No market prices found</h3>
            <p>
              Try clearing the filters or add price records to your database.
            </p>
          </div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Crop / पीक</th>
                  <th>Market / बाजार</th>
                  <th>District</th>
                  <th>Minimum</th>
                  <th>Modal</th>
                  <th>Maximum</th>
                  <th>Unit</th>
                </tr>
              </thead>
              <tbody>
                {prices.map((price) => (
                  <tr key={price.id}>
                    <td>{price.priceDate}</td>
                    <td>
                      <strong>{price.crop?.name ?? "—"}</strong>
                      <div className="marathi-name">
                        {price.crop?.nameMr ?? ""}
                      </div>
                    </td>
                    <td>{price.mandi?.name ?? "—"}</td>
                    <td>{price.mandi?.district ?? "—"}</td>
                    <td>{formatPrice(price.minPrice)}</td>
                    <td className="modal-price">
                      {formatPrice(price.modalPrice)}
                    </td>
                    <td>{formatPrice(price.maxPrice)}</td>
                    <td>{price.unit ?? "—"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {!loading && prices.length > 0 && (
          <p className="record-count">
            Showing {prices.length} price record(s)
          </p>
        )}
      </section>

      <footer className="page-footer">
        Mandi Tracker · Maharashtra · Market data with source and date
      </footer>
    </main>
  );
}

export default App;
