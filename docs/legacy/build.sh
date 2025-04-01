#!/bin/bash

# Exit on error
set -e

# Define directories
DOCS_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
API_DIR="$DOCS_DIR/api"
USERS_GUIDE_DIR="$DOCS_DIR/users-guide"
DEVELOPERS_GUIDE_DIR="$DOCS_DIR/developers-guide"
TECHNICAL_DIR="$DOCS_DIR/technical"

# Copy API documentation if it exists
if [[ -d "$API_DIR" ]]; then
  echo "Copying API documentation..."
  # No need to copy - it's already there
fi

# Function to add front matter to AsciiDoc files
add_front_matter() {
  local source_dir=$1
  local collection=$2
  local title=$3
  
  echo "Adding front matter to $title documentation..."
  
  # Process all AsciiDoc files
  for file in "$source_dir"/*.adoc; do
    if [ -f "$file" ]; then
      # Check if front matter already exists (looks for --- at the beginning of the file)
      if grep -q "^---" "$file"; then
        echo "Front matter already exists in $file, skipping..."
        continue
      fi
      
      filename=$(basename "$file")
      
      # Get the title from the file (first line with = at the beginning)
      page_title=$(grep "^= " "$file" | head -n 1 | sed 's/^= //')
      if [ -z "$page_title" ]; then
        # If no title found, use filename as title
        page_title="${filename%.adoc}"
      fi
      
      # Create a temporary file
      tmp_file=$(mktemp)
      
      # Add front matter to the beginning of the file
      echo "---" > "$tmp_file"
      echo "layout: default" >> "$tmp_file"
      echo "title: \"$page_title\"" >> "$tmp_file"
      
      # Add parent based on collection
      if [ "$collection" = "users-guide" ]; then
        echo "parent: \"User's Guide\"" >> "$tmp_file"
      elif [ "$collection" = "developers-guide" ]; then
        echo "parent: \"Developer's Guide\"" >> "$tmp_file"
      elif [ "$collection" = "technical" ]; then
        echo "parent: \"Technical Documentation\"" >> "$tmp_file"
      fi
      
      echo "---" >> "$tmp_file"
      
      # Append the original content
      cat "$file" >> "$tmp_file"
      
      # Replace the original file
      mv "$tmp_file" "$file"
    fi
  done
}

# Build Jekyll site with AsciiDoc integration
echo "Building User's Guide documentation..."
add_front_matter "$USERS_GUIDE_DIR" "users-guide" "User's Guide"

echo "Building Developer's Guide documentation..."
add_front_matter "$DEVELOPERS_GUIDE_DIR" "developers-guide" "Developer's Guide"

echo "Building Technical Documentation documentation..."
add_front_matter "$TECHNICAL_DIR" "technical" "Technical Documentation"

# Convert management.md to AsciiDoc if it exists
if [[ -f "$DOCS_DIR/management.md" && ! -f "$DOCS_DIR/management.adoc" ]]; then
  echo "Converting management.md to AsciiDoc..."
  if command -v pandoc &> /dev/null; then
    pandoc -f markdown -t asciidoc -o "$DOCS_DIR/management.adoc" "$DOCS_DIR/management.md"
  else
    echo "WARNING: pandoc not found. Creating simple AsciiDoc from management.md."
    echo "= Management Endpoints" > "$DOCS_DIR/management.adoc"
    echo ":toc: left" >> "$DOCS_DIR/management.adoc"
    echo ":sectnums:" >> "$DOCS_DIR/management.adoc"
    echo "" >> "$DOCS_DIR/management.adoc"
    cat "$DOCS_DIR/management.md" | sed 's/^#/==/g' | sed 's/^##/===/g' >> "$DOCS_DIR/management.adoc"
  fi
fi

# Add front matter to management.adoc if it exists
if [[ -f "$DOCS_DIR/management.adoc" ]]; then
  echo "Building management page..."
  
  # Check if front matter already exists
  if grep -q "^---" "$DOCS_DIR/management.adoc"; then
    echo "Front matter already exists in management.adoc, skipping..."
  else
    # Create a temporary file
    tmp_file=$(mktemp)
    
    # Add front matter to the beginning of the file
    echo "---" > "$tmp_file"
    echo "layout: default" >> "$tmp_file"
    echo "title: \"Management API\"" >> "$tmp_file"
    echo "nav_order: 5" >> "$tmp_file"
    echo "---" >> "$tmp_file"
    
    # Append the original content
    cat "$DOCS_DIR/management.adoc" >> "$tmp_file"
    
    # Replace the original file
    mv "$tmp_file" "$DOCS_DIR/management.adoc"
  fi
fi

echo "Documentation build complete. You can now run 'bundle exec jekyll serve' to preview."