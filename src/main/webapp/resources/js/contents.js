/**
 * 
 */
function showCytoscape(divId, initialData){
	var cy = cytoscape({
	
	  container: document.getElementById(divId), // container to render in
	
	  elements: initialData,
	
	  style: [ // the stylesheet for the graph
	    {
	      selector: 'node',
	      style: {
	        'background-color': '#666',
	        'label': 'data(id)'
	      }
	    },
	    {
            selector: '.eh-handle',
            style: {
              'background-color': 'red',
              'width': 12,
              'height': 12,
              'shape': 'ellipse',
              'overlay-opacity': 0,
              'border-width': 12, // makes the handle easier to hit
              'border-opacity': 0
            }
          },

          {
            selector: '.eh-hover',
            style: {
              'background-color': 'red'
            }
          },

          {
            selector: '.eh-source',
            style: {
              'border-width': 2,
              'border-color': 'red'
            }
          },

          {
            selector: '.eh-target',
            style: {
              'border-width': 2,
              'border-color': 'red'
            }
          },

          {
            selector: '.eh-preview, .eh-ghost-edge',
            style: {
              'background-color': 'red',
              'line-color': 'red',
              'target-arrow-color': 'red',
              'source-arrow-color': 'red'
            }
          },

          {
            selector: '.eh-ghost-edge.eh-preview-active',
            style: {
              'opacity': 0
            }
          },
	
	    {
	      selector: 'edge',
	      style: {
	        'width': 3,
	        'line-color': '#ccc',
	        'target-arrow-color': '#ccc',
	        'target-arrow-shape': 'triangle',
	        'curve-style': 'bezier'
	      }
	    }
	  ],
	
	  /*layout: {
	    name: 'grid',
	    rows: 1
	  }*/
	  layout: {
          name: 'concentric',
          concentric: function(n){ return n.id() === 'j' ? 200 : 0; },
          levelWidth: function(nodes){ return 100; },
          minNodeSpacing: 100
        },
	
	});
	
	  var eh = cy.edgehandles({
          snap: true
        });
	  
	  eh.enableDrawMode();
}