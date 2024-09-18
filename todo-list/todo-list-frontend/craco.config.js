module.exports = {
  style: {
    postcss: {
      plugins: [
        require('autoprefixer')({
          overrideBrowserslist: ['>0.2%'],
        }),
      ],
    },
  },
};
