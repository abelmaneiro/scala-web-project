const path = require('path')
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

module.exports = {
  entry: './ui/entry.js',
  output: {
    path: path.resolve(__dirname, 'public/compiled'),
    filename: 'bundle.js'
  },
  module: {
    rules: [              // Webpack uses loaders to work with anything other than standard JavaScript files
      {                   // Specifying the regex that limits the babel-loader to process only js and jsx files
        test: /\.jsx?$/,  // During the build the babel-loader will read the instructions from ..babelrc file
        include: /ui/,
        use: {
          loader: 'babel-loader'
        }
      },
      {
        test: /\.scss$/,
        use: [MiniCssExtractPlugin.loader, 'css-loader', 'sass-loader']
      }
    ]
  },
  plugins: [
    new MiniCssExtractPlugin({ filename: "styles.css" })
  ]
}
