#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
EXPORTS_DIR="${SCRIPT_DIR}/exports"
OUTPUT_DIR="${SCRIPT_DIR}/../assets/images/structurizr"

# Create the export and output directories if they don't exist
mkdir -p "$EXPORTS_DIR"
mkdir -p "$OUTPUT_DIR"

echo "Step 1: Pulling the latest Structurizr CLI Docker image"
docker pull structurizr/cli:latest

echo "Step 2: Generating PlantUML files from Structurizr DSL"
# Export workspace.dsl to PlantUML
echo "Exporting workspace.dsl to PlantUML..."
docker run --rm -v "${SCRIPT_DIR}:/usr/local/structurizr" structurizr/cli:latest export \
  -workspace /usr/local/structurizr/workspace.dsl \
  -format plantuml/c4plantuml \
  -output /usr/local/structurizr/exports

echo "Step 3: Generate SVG files with PlantUML"

# Pull PlantUML Docker image
echo "Pulling PlantUML Docker image..."
docker pull plantuml/plantuml:latest

# Process each PlantUML file to SVG
for puml_file in "${EXPORTS_DIR}"/*.puml; do
  if [ -f "$puml_file" ]; then
    file_name=$(basename "$puml_file")
    output_file="${OUTPUT_DIR}/$(basename "$puml_file" .puml).svg"
    echo "Generating SVG for $file_name to $output_file..."
    
    # Make sure we have full paths
    puml_abs_path=$(realpath "$puml_file")
    output_dir_abs_path=$(realpath "$OUTPUT_DIR")
    
    # Run PlantUML with Docker
    docker run --rm -v "$(dirname "$puml_abs_path"):/work" plantuml/plantuml -tsvg "/work/$(basename "$puml_file")"
    
    # Move SVG to the output directory
    if [ -f "$(dirname "$puml_abs_path")/$(basename "$puml_file" .puml).svg" ]; then
      mv "$(dirname "$puml_abs_path")/$(basename "$puml_file" .puml).svg" "$output_dir_abs_path/"
      echo "✓ SVG file created successfully"
    else
      echo "⚠ Failed to create SVG file"
    fi
  fi
done

echo "Diagram generation complete. SVG files are available in: $OUTPUT_DIR"

# List the generated SVG files
echo "Generated SVG files:"
find "$OUTPUT_DIR" -name "*.svg" | sort | while read -r file; do
  file_size=$(stat -f%z "$file" 2>/dev/null || stat -c%s "$file" 2>/dev/null)
  echo " - $(basename "$file") (Size: $file_size bytes)"
done