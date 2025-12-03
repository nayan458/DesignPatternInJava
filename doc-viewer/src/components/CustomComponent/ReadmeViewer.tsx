import { serveReadmeAsHtml } from "@/utils/ReadmeUtils/fetchReadmeFromStatic";
import type React from "react";
import { useState, useEffect } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

export interface ReadmeViewerProps {
  content?: string;
  filePath?: string;
  component?: React.ReactNode;
  className?: string;
  onError?: (error: Error) => void;
  onLoad?: () => void;
}

const ReadmeViewer: React.FC<ReadmeViewerProps> = ({
  content,
  filePath,
  component,
  className = "",
  onError,
  onLoad,
}) => {
  const [markdownContent, setMarkdownContent] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadContent = async () => {
      try {
        setLoading(true);
        setError(null);

        if (component) {
          setLoading(false);
          onLoad?.();
          return;
        }

        if (content) {
          setMarkdownContent(content);
        } else if (filePath) {
          const fetchedContent = await serveReadmeAsHtml(filePath);
          setMarkdownContent(fetchedContent);
        } else {
          throw new Error("No content, filePath, or component provided");
        }

        onLoad?.();
      } catch (err) {
        const errorMessage = err instanceof Error ? err.message : "Unknown error";
        setError(errorMessage);
        if (onError && err instanceof Error) {
          onError(err);
        }
      } finally {
        setLoading(false);
      }
    };

    loadContent();
  }, [content, filePath, component, onError, onLoad]);

  if (component) {
    return <div className={className}>{component}</div>;
  }

  if (loading) {
    return (
      <div className={className}>
        <div style={{ textAlign: "center", padding: "2rem", color: "#666" }}>
          Loading README...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={className}>
        <div
          style={{
            background: "#fef2f2",
            border: "1px solid #fecaca",
            color: "#991b1b",
            padding: "1rem",
            borderRadius: "6px",
          }}
        >
          <strong>Error:</strong> {error}
        </div>
      </div>
    );
  }

  return (
    <div className={`markdown-body ${className}`}>
      <ReactMarkdown remarkPlugins={[remarkGfm]}>
        {markdownContent}
      </ReactMarkdown>
    </div>
  );
};

export default ReadmeViewer;

