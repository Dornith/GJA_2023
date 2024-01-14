// read and display first 5 lines of the csv file
const csvReader = new FileReader();
csvReader.onload = function (e) {
    const content = e.target.result;
    const lines = content.split("\n").slice(0, 5);

    const displayedLines = lines.slice(0, 4);
    if (lines.length === 5) {
        displayedLines.push("...");
    }

    document.getElementById("file-content").textContent = displayedLines.join("\n");
};

function displayCSV(event) {
    const file = event.target.files[0];
    if (file) {
        csvReader.readAsText(file);
    }
}
