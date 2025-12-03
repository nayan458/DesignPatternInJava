export const fetchReadmeFromStatic = async (path: string): Promise<string> => {
  try {
    const response = await fetch(path);
    if (!response.ok) {
      throw new Error(`Failed to fetch README: ${response.statusText}`);
    }
    return await response.text();
  } catch (error) {
    console.error('Error fetching README:', error);
    throw error;
  }
};

export const serveReadmeAsHtml = async (filePath: string): Promise<string> => {
  return await fetchReadmeFromStatic(filePath);
};
