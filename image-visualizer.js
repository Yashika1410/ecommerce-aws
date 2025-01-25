const rawBody = pm.response.stream; // Get the binary response
const base64Image = btoa(
    new Uint8Array(rawBody)
        .reduce((data, byte) => data + String.fromCharCode(byte), '')
);

// Render the image using Postman's visualizer
pm.visualizer.set(`
    <div style="text-align: center;">
        <h3>Image Preview</h3>
        <img src="data:image/png;base64,${base64Image}" alt="Image" style="max-width: 100%; height: auto; border: 1px solid #ddd; padding: 5px; border-radius: 8px;">
    </div>
`);