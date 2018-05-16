const webpack = require('webpack');
const path = require('path');

const HtmlWebpackPlugin = require('html-webpack-plugin');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');

const BUILD_DIR = path.resolve(__dirname, 'public');
const NODE_DIR = path.resolve(__dirname, 'node_modules');
const TEST_DIR = path.resolve(__dirname, '__tests__');

const APP_DIR = path.resolve(__dirname, 'app');

const ENV = 'production';

module.exports = {
  entry: [
    'babel-polyfill',
    APP_DIR
  ],
  output: {
    path: BUILD_DIR,
    filename: '[name].bundle.js',
    chunkFilename: '[hash].js'
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: [
          NODE_DIR,
          TEST_DIR
        ],
        include: [
          APP_DIR
        ],
        loader: 'babel-loader',
        options: {
          plugins: [
            'babel-plugin-syntax-dynamic-import',
            'transform-flow-strip-types',
            'transform-object-rest-spread'
          ],
          presets: [
            'env',
            'react',
            'flow'
          ]
        }
      }, {
        test: /\.css$/,
        loader: 'style-loader!css-loader'
      }, {
        test: /\.(jpe?g|png|svg|ai)$/,
        loader: 'file-loader?publicPath=public/'
      }, {
        test: /\.(woff|woff2|eot|ttf)$/,
        loader: 'url-loader?limit=100000'
      }
    ]
  },
  mode: JSON.stringify(ENV),
  plugins: [
    new UglifyJSPlugin(),
    new HtmlWebpackPlugin({
      filename: '../index.html',
      template: 'templates/index.template.ejs'
    })
  ]
};
