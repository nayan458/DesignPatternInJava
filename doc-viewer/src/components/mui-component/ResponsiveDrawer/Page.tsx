import type React from "react";
import ReadmeViewer, { type ReadmeViewerProps } from "../../CustomComponent/ReadmeViewer";

const Page: React.FC<ReadmeViewerProps> = ({filePath}) => {

  return (
    <>
      <div style={{ padding: "2rem" }}>
        <section style={{ marginBottom: "3rem" }}>
          <div style={{ padding: "1rem" }}>
            <ReadmeViewer filePath={filePath}/>
          </div>
        </section>
      </div>
    </>
  );
};

export default Page;