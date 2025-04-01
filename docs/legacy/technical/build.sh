#!/bin/bash

# Create build directory if it doesn't exist
mkdir -p build

# Copy images directory
cp -r images build/

# Convert all AsciiDoc files to HTML
for file in *.adoc; do
  echo "Converting $file to HTML..."
  asciidoctor -o build/$(basename $file .adoc).html $file
done

# Generate index file
cat > build/index.html << EOF
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>GeoServer ACL Technical Documentation</title>
  <style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      margin: 40px;
      line-height: 1.6;
      color: #333;
    }
    h1 { color: #2c3e50; }
    h2 { color: #3498db; margin-top: 30px; }
    ul { list-style-type: square; }
    a { color: #2980b9; text-decoration: none; }
    a:hover { text-decoration: underline; }
    .container { max-width: 800px; margin: 0 auto; }
  </style>
</head>
<body>
  <div class="container">
    <h1>GeoServer ACL Technical Documentation</h1>
    <p>This documentation follows the arc42 template and provides a comprehensive technical overview of the GeoServer ACL system.</p>
    
    <h2>Documentation Sections</h2>
    <ul>
EOF

# Add links to all generated HTML files
for file in *.adoc; do
  title=$(grep -m 1 "^= " $file | sed 's/^= //')
  htmlfile=$(basename $file .adoc).html
  echo "      <li><a href=\"$htmlfile\">$title</a></li>" >> build/index.html
done

cat >> build/index.html << EOF
    </ul>
    
    <h2>Additional Resources</h2>
    <ul>
      <li><a href="https://github.com/geoserver/geoserver-acl">GitHub Repository</a></li>
      <li><a href="../users-guide.html">User's Guide</a></li>
      <li><a href="../api/index.html">API Documentation</a></li>
    </ul>
  </div>
</body>
</html>
EOF

echo "Documentation build complete. Output in the 'build' directory."