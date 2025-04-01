# GeoServer ACL Documentation

This directory contains the documentation for GeoServer ACL, organized into multiple sections.

## Documentation Structure

- **[User's Guide](users-guide/)**: Complete guide to installing, configuring, and using GeoServer ACL
- **[Developer's Guide](developers-guide/)**: Guide for developers working with the codebase
- **[Technical Documentation](technical/)**: Architecture and design documentation (arc42 format)
- **Management API**: Information about management endpoints
- **API Documentation**: OpenAPI specification for the REST API

## Building the Documentation

### Prerequisites

#### macOS

macOS comes with a system Ruby installation (`/usr/bin/ruby`), but it's better to use a separate Ruby installation for development. There are several options:

**Option 1: Using Homebrew**

1. Install Ruby using Homebrew:
   ```bash
   brew install ruby
   ```

2. Add the Homebrew Ruby to your PATH in `~/.zshrc` or `~/.bash_profile`:
   ```bash
   # For Intel Macs
   echo 'export PATH="/usr/local/opt/ruby/bin:$PATH"' >> ~/.zshrc
   
   # For Apple Silicon Macs
   echo 'export PATH="/opt/homebrew/opt/ruby/bin:$PATH"' >> ~/.zshrc
   ```

3. Restart your terminal or source your profile:
   ```bash
   source ~/.zshrc  # or ~/.bash_profile
   ```

4. Verify you're using the Homebrew Ruby:
   ```bash
   which ruby  # Should show /usr/local/opt/ruby/bin/ruby or /opt/homebrew/opt/ruby/bin/ruby
   ruby -v    # Should show the version you installed
   ```

5. Install Bundler:
   ```bash
   gem install bundler
   ```

**Option 2: Using rbenv (Recommended)**

1. Install rbenv using Homebrew:
   ```bash
   brew install rbenv
   ```

2. Set up rbenv in your shell:
   ```bash
   rbenv init
   ```

3. Follow the printed instructions to add to your profile, then restart your terminal.

4. Install a Ruby version:
   ```bash
   rbenv install 3.1.3
   rbenv global 3.1.3
   ```

5. Verify the installation:
   ```bash
   ruby -v  # Should show the version you installed
   ```

6. Install Bundler:
   ```bash
   gem install bundler
   ```

#### Linux (Ubuntu/Debian)

1. Install Ruby and development dependencies:
   ```bash
   sudo apt-get update
   sudo apt-get install ruby-full build-essential zlib1g-dev
   ```

2. Set up Ruby gems to install to your user directory:
   ```bash
   echo '# Install Ruby Gems to ~/gems' >> ~/.bashrc
   echo 'export GEM_HOME="$HOME/gems"' >> ~/.bashrc
   echo 'export PATH="$HOME/gems/bin:$PATH"' >> ~/.bashrc
   source ~/.bashrc
   ```

3. Install Bundler:
   ```bash
   gem install bundler
   ```

#### Linux (Fedora/CentOS/RHEL)

1. Install Ruby and development dependencies:
   ```bash
   sudo dnf install ruby ruby-devel openssl-devel redhat-rpm-config @development-tools
   ```

2. Set up Ruby gems to install to your user directory:
   ```bash
   echo '# Install Ruby Gems to ~/gems' >> ~/.bashrc
   echo 'export GEM_HOME="$HOME/gems"' >> ~/.bashrc
   echo 'export PATH="$HOME/gems/bin:$PATH"' >> ~/.bashrc
   source ~/.bashrc
   ```

3. Install Bundler:
   ```bash
   gem install bundler
   ```

### Local Development

To build the documentation locally:

1. Clone the repository (if you haven't already):
   ```bash
   git clone https://github.com/geoserver/geoserver-acl.git
   cd geoserver-acl
   ```

2. Install dependencies:
   ```bash
   cd docs
   bundle install
   ```

3. Run the build script:
   ```bash
   chmod +x build.sh  # Make sure the script is executable
   ./build.sh
   ```

4. The output will be in the `_site` directory

### Preview with Jekyll

To preview the documentation with Jekyll's development server:

```bash
cd docs
bundle exec jekyll serve
```

Then browse to http://localhost:4000

### Troubleshooting

#### Ruby Version Issues

If you encounter Ruby version issues, consider using a version manager like `rbenv` or `rvm`:

**For rbenv:**
```bash
# Install rbenv (macOS)
brew install rbenv
rbenv init

# Install rbenv (Linux)
git clone https://github.com/rbenv/rbenv.git ~/.rbenv
echo 'export PATH="$HOME/.rbenv/bin:$PATH"' >> ~/.bashrc
echo 'eval "$(rbenv init -)"' >> ~/.bashrc
source ~/.bashrc

# Install Ruby
rbenv install 3.1.0
rbenv global 3.1.0
```

#### Bundle Install Errors

If you encounter permission errors during `bundle install`:

```bash
# Install gems locally to the project
bundle config set --local path 'vendor/bundle'
bundle install
```

#### Asciidoctor Errors

If you encounter issues with Asciidoctor:

```bash
# Install Asciidoctor manually
gem install asciidoctor

# If you need diagram support
gem install asciidoctor-diagram
```

## Documentation Format

- Main pages are written in Markdown
- User's Guide, Developer's Guide, and Technical Documentation are written in AsciiDoc
- API documentation is generated from OpenAPI specification

## Contributing

When contributing to the documentation:

1. Create a feature branch
2. Make your changes
3. Test locally using the build script
4. Submit a pull request

## Automated Builds

Documentation is automatically built and deployed to GitHub Pages when changes are pushed to the main branch.

## License

The documentation is licensed under the same license as the GeoServer ACL project.

