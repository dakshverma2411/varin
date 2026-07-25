import { useState } from "react";
import { VarinField } from "./VarinField";
import type { Variable, VarinFieldChangeEvent } from "./types";

const sampleVariables: Variable[] = [
  {
    name: "app_name",
    description: "Application Name",
    required: true,
    dataType: "STRING",
    value: { type: "FIXED", value: ["MyApp"] },
  },
  {
    name: "environment",
    description: "Deployment Environment",
    required: true,
    dataType: "STRING",
    value: {
      type: "NON_FIXED",
      rule: {
        type: "ONE_OF",
        options: [
          { value: "dev", displayValue: "Development" },
          { value: "staging", displayValue: "Staging" },
          { value: "prod", displayValue: "Production", description: "Live environment" },
        ],
      },
      defaultValue: { value: ["dev"] },
    },
  },
  {
    name: "age",
    description: "User Age",
    required: true,
    dataType: "INT",
    value: {
      type: "NON_FIXED",
      rule: { type: "RANGE", min: "18", minInclusive: true, max: "60", maxInclusive: true },
      defaultValue: { value: ["25"] },
    },
  },
  {
    name: "port",
    description: "Server Port",
    required: false,
    dataType: "INT",
    value: {
      type: "NON_FIXED",
      rule: { type: "RANGE", min: "1024", minInclusive: true, max: "65535", maxInclusive: true },
      defaultValue: { value: ["8080"] },
    },
  },
  {
    name: "username",
    description: "Your username",
    required: false,
    dataType: "STRING",
    value: {
      type: "NON_FIXED",
      rule: { type: "ANY", hint: "Enter a username..." },
    },
  },
];

function App() {
  const [log, setLog] = useState<string[]>([]);

  const handleChange = (name: string) => (event: VarinFieldChangeEvent) => {
    const msg = `${name}: ${JSON.stringify(event.value)} ${event.validation.isValid ? "✓" : `✗ ${event.validation.error}`}`;
    setLog((prev) => [msg, ...prev].slice(0, 20));
  };

  return (
    <div style={{ fontFamily: "system-ui", padding: "2rem", maxWidth: 600 }}>
      <h1>Varin UI Demo</h1>
      <div style={{ display: "flex", flexDirection: "column", gap: "1.5rem" }}>
        {sampleVariables.map((v) => (
          <div key={v.name} style={{ border: "1px solid #ccc", padding: "1rem", borderRadius: 8 }}>
            <label style={{ fontWeight: 600, display: "block", marginBottom: 8, alignItems: "center", gap: 4 }}>
              {v.displayName ?? v.name}
              {v.required && <span style={{ color: "red" }}> *</span>}
              {v.description && (
                <span
                  title={v.description}
                  style={{ cursor: "help", fontSize: "0.85em", opacity: 0.6 }}
                  role="img"
                  aria-label={v.description}
                >
                  ⓘ
                </span>
              )}
            </label>
            <VarinField variable={v} onChange={handleChange(v.name)} />
          </div>
        ))}
      </div>
      <h2 style={{ marginTop: "2rem" }}>Event Log</h2>
      <pre style={{ fontSize: 12, background: "#f5f5f5", padding: "1rem", maxHeight: 200, overflow: "auto" }}>
        {log.join("\n") || "Interact with fields above..."}
      </pre>
    </div>
  );
}

export default App;
