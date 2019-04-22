var webpack = require("webpack");
var merge = require("webpack-merge");
var common = require("./webpack.common.js");
var UglifyJsPlugin = require("uglifyjs-3-webpack-plugin");

module.exports = merge(common, {
    devtool: "source-map",
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: JSON.stringify('production')
            }
        }),
        new UglifyJsPlugin({
            uglifyOptions: {
                warnings: false,
                ie8: false,
                output: {
                    comments: false
                }
            }
        })
    ]
});