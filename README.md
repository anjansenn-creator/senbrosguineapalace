# Sen Bros Guinea Palace website

A responsive, static three-page website prepared for GitHub Pages and Google Search Console.

## Preview locally

Open `index.html` in a browser, or open this folder in VS Code and use a local preview extension such as Live Server.

## Publish with GitHub Pages

The site URLs are configured for [anjansenn-creator/senbrosguineapalace](https://github.com/anjansenn-creator/senbrosguineapalace). The expected Pages address is `https://anjansenn-creator.github.io/senbrosguineapalace/`.

Upload the contents of this `outputs` folder to the **root** of that repository, then:

1. Open the repository's **Settings → Pages**.
2. Under **Build and deployment**, choose **Deploy from a branch**, select `main` and `/ (root)`, then save.
3. Wait for GitHub Pages to publish the site. Confirm the final address shown in Pages settings matches the expected URL above.

## Finish SEO setup after publishing

The canonical URLs, social sharing metadata, structured business information, `robots.txt`, and `sitemap.xml` are configured for the expected GitHub Pages address above. If GitHub shows a different address, update the URLs in these files before submitting the site:

- `index.html`, `products.html`, and `contact.html` (canonical, social share and structured data URLs)
- `robots.txt` (sitemap address)
- `sitemap.xml` (page addresses)

Commit and push any URL corrections, then wait for GitHub Pages to publish them.

### Connect Google Search Console

1. Open [Google Search Console](https://search.google.com/search-console/) and add this **URL-prefix** property: `https://anjansenn-creator.github.io/senbrosguineapalace/`.
2. Choose the **HTML tag** verification method. Copy the full `google-site-verification` meta tag Google provides and paste it in the `<head>` of `index.html` where the verification comment is. Commit and push the change.
3. Once the site is live with that tag, return to Search Console and click **Verify**.
4. In the verified property, open **Sitemaps**, enter `sitemap.xml`, and submit it. The sitemap should then be available at `https://anjansenn-creator.github.io/senbrosguineapalace/sitemap.xml`.
5. Use **URL inspection** to request indexing for the home, jewellery, and contact pages.

The HTML verification tag cannot be completed until Google gives you the property-specific token. Keep the tag in the homepage after verification so Search Console can recheck ownership.

## Before sharing the site

- Replace the `href="#"` social profile links in `index.html`, `products.html`, and `contact.html` with the store's actual Instagram and Facebook URLs.
- The enquiry form opens the visitor's email app with a message addressed to `anjansenn@gmail.com`. GitHub Pages does not process form submissions itself. For direct web submissions without an email app, connect a hosted form endpoint (such as Formspree) and update the form handler in `script.js`.
- Product imagery is loaded from Unsplash and Google Fonts are loaded from Google Fonts, so those visuals need an internet connection.
- Confirm the map pin, store hours, and product photography/details before launch. The current map uses the supplied street address.

