import { useState } from "react";

function App() {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [darkMode, setDarkMode] = useState(false);

  const [summaryLength, setSummaryLength] = useState("standard");
  const [summaryStyle, setSummaryStyle] = useState("general");

  const handleUpload = async () => {
    if (!file) {
      setMessage("Please select a PDF first.");
      return;
    }

    setLoading(true);
    setMessage("");

    const formData = new FormData();
    formData.append("file", file);
    formData.append("length", summaryLength);
    formData.append("style", summaryStyle);

    try {
      const response = await fetch("http://localhost:8080/api/upload", {
        method: "POST",
        body: formData,
      });

      const result = await response.text();
      setMessage(result);
    } catch (error) {
      setMessage("Error connecting to backend.");
    } finally {
      setLoading(false);
    }
  };

  const formatSummary = (text) => {
    const lines = text.split("\n");

    return lines.map((line, index) => {
      const trimmed = line.trim();

      if (!trimmed) {
        return <div key={index} style={{ height: "10px" }} />;
      }

      if (trimmed.startsWith("##")) {
        return (
          <h3
            key={index}
            style={{
              marginTop: "24px",
              marginBottom: "10px",
              color: darkMode ? "#7dd3fc" : "#2563eb",
              fontSize: "20px",
              fontWeight: "700",
            }}
          >
            {trimmed.replace(/^##\s*/, "")}
          </h3>
        );
      }

      if (trimmed.startsWith("-")) {
        return (
          <li
            key={index}
            style={{
              marginBottom: "8px",
              lineHeight: "1.6",
              fontWeight: "500",
            }}
          >
            {trimmed.substring(1).trim()}
          </li>
        );
      }

      return (
        <p
          key={index}
          style={{
            lineHeight: "1.7",
            margin: "7px 0",
          }}
        >
          {trimmed}
        </p>
      );
    });
  };

  const theme = darkMode
    ? {
      background: "#111827",
      card: "#1f2937",
      text: "#f9fafb",
      secondaryText: "#cbd5e1",
      border: "#374151",
      button: "#2563eb",
    }
    : {
      background: "#f3f6fa",
      card: "#ffffff",
      text: "#111827",
      secondaryText: "#6b7280",
      border: "#e5e7eb",
      button: "#2563eb",
    };

  const optionButtonStyle = (selected) => ({
    padding: "9px 16px",
    borderRadius: "8px",
    border: `1px solid ${selected ? "#2563eb" : theme.border}`,
    backgroundColor: selected ? "#2563eb" : theme.card,
    color: selected ? "white" : theme.text,
    cursor: "pointer",
    fontWeight: "600",
  });

  return (
    <div
      style={{
        minHeight: "100vh",
        backgroundColor: theme.background,
        color: theme.text,
        padding: "40px",
        fontFamily: "Arial, sans-serif",
        transition: "0.3s ease",
      }}
    >
      <div style={{ maxWidth: "850px", margin: "0 auto" }}>

        {/* Header */}
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "10px",
          }}
        >
          <div>
            <h1
              style={{
                margin: 0,
                fontSize: "34px",
                fontWeight: "800",
                letterSpacing: "-0.5px",
                color: darkMode ? "#ffffff" : "#111827",
              }}
            >
              Document Summary Assistant
            </h1>

            <p
              style={{
                color: theme.secondaryText,
                fontSize: "16px",
                marginTop: "10px",
              }}
            >
              Upload a document and get an AI-generated structured summary.
            </p>
          </div>

          <button
            onClick={() => setDarkMode(!darkMode)}
            style={{
              padding: "9px 15px",
              borderRadius: "8px",
              border: `1px solid ${theme.border}`,
              backgroundColor: theme.card,
              color: theme.text,
              cursor: "pointer",
              fontSize: "14px",
              fontWeight: "600",
            }}
          >
            {darkMode ? "☀️ Light" : "🌙 Dark"}
          </button>
        </div>

        {/* Upload Card */}
        <div
          style={{
            backgroundColor: theme.card,
            padding: "25px",
            borderRadius: "14px",
            border: `1px solid ${theme.border}`,
            boxShadow: darkMode
              ? "0 4px 15px rgba(0,0,0,0.25)"
              : "0 4px 15px rgba(0,0,0,0.06)",
            marginTop: "25px",
          }}
        >
          <h2 style={{ marginTop: 0 }}>Upload Document</h2>

          <p style={{ color: theme.secondaryText }}>
            Select a PDF file to generate its summary.
          </p>

          <input
            type="file"
            accept=".pdf"
            onChange={(e) => setFile(e.target.files[0])}
          />

          {/* Summary Length */}
          <h3
            style={{
              marginTop: "25px",
              marginBottom: "12px",
              fontSize: "16px",
            }}
          >
            Summary Length
          </h3>

          <div
            style={{
              display: "flex",
              gap: "10px",
              marginBottom: "25px",
              flexWrap: "wrap",
            }}
          >
            {["brief", "standard", "detailed"].map((length) => (
              <button
                key={length}
                onClick={() => setSummaryLength(length)}
                style={optionButtonStyle(summaryLength === length)}
              >
                {length.charAt(0).toUpperCase() + length.slice(1)}
              </button>
            ))}
          </div>

          {/* Summary Style */}
          <h3
            style={{
              marginBottom: "12px",
              fontSize: "16px",
            }}
          >
            Summary Style
          </h3>

          <div
            style={{
              display: "flex",
              gap: "10px",
              marginBottom: "25px",
              flexWrap: "wrap",
            }}
          >
            {["general", "academic", "technical", "resume"].map((style) => (
              <button
                key={style}
                onClick={() => setSummaryStyle(style)}
                style={optionButtonStyle(summaryStyle === style)}
              >
                {style.charAt(0).toUpperCase() + style.slice(1)}
              </button>
            ))}
          </div>

          <button
            onClick={handleUpload}
            disabled={loading}
            style={{
              padding: "11px 22px",
              borderRadius: "8px",
              border: "none",
              cursor: loading ? "not-allowed" : "pointer",
              backgroundColor: theme.button,
              color: "white",
              fontSize: "15px",
              fontWeight: "600",
            }}
          >
            {loading ? "Generating Summary..." : "Upload & Summarize"}
          </button>
        </div>

        {/* Summary Card */}
        {message && (
          <div
            style={{
              marginTop: "30px",
              backgroundColor: theme.card,
              padding: "30px",
              borderRadius: "14px",
              border: `1px solid ${theme.border}`,
              boxShadow: darkMode
                ? "0 4px 15px rgba(0,0,0,0.25)"
                : "0 4px 15px rgba(0,0,0,0.06)",
            }}
          >
            <h2
              style={{
                marginTop: 0,
                fontSize: "26px",
                fontWeight: "800",
                color: darkMode ? "#60a5fa" : "#1d4ed8",
                borderBottom: `2px solid ${darkMode ? "#374151" : "#dbeafe"
                  }`,
                paddingBottom: "12px",
              }}
            >
              📄 Document Summary
            </h2>

            <div>{formatSummary(message)}</div>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;