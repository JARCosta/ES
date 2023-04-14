//const pixelmatch = require('pixelmatch');
const PNG = require('pngjs').PNG;

function compareScreenshots(name1, name2, diffName) {
    cy.readFile(`tests/e2e/specs/teacherdashboard/${name1}`, 'base64').then((img1) => {
    cy.readFile(`tests/e2e/screenshots/teacherdashboard.js/${name2}`, 'base64').then((img2) => {
      const img1Png = PNG.sync.read(Buffer.from(img1, 'base64'));
      const img2Png = PNG.sync.read(Buffer.from(img2, 'base64'));
      const { width, height } = img1Png;
      const diff = new PNG({ width, height });
      const numDiffPixels = pixelmatch(
        img1Png.data,
        img2Png.data,
        diff.data,
        width,
        height,
        { threshold: 0.1 }
      );
      if (numDiffPixels > 0) {
        cy.writeFile(`cypress/screenshots/${diffName}.png`, PNG.sync.write(diff), 'binary');
      }
      expect(numDiffPixels).to.equal(0);
    });
  });
}
  
  module.exports = {
    compareScreenshots
  };
