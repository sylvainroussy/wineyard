package fr.srosoft.wineyard.modules.cave;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import fr.srosoft.wineyard.core.model.entities.Chemical;
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.model.entities.Measure;
import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.model.entities.TraceLine;
import fr.srosoft.wineyard.core.services.CaveService;
import fr.srosoft.wineyard.utils.Constants.MEASURE_TYPE;

@SuppressWarnings("serial")
public class ContainerDetailForm implements Serializable {

	private CaveService caveService;
	
	private Measure measure = new Measure();
	private String measureType;
	private LineChartModel densityChart;
	private Tank currentTank;
	private String currentDensityView ="grid";
	private List<Chemical> selectedChemicals;
	
	
	public ContainerDetailForm(CaveService caveService) {
		this.caveService = caveService;
	}
	
	public void clear() {
		measure = new Measure();
		measureType = null;
		this.createDensityChart();	
	}
	
	public String getCurrentDensityView() {
		return currentDensityView;
	}

	public void setCurrentDensityView(String currentDensityView) {
		this.currentDensityView = currentDensityView;
	}
	
	
	public Tank getCurrentTank() {
		return currentTank;
	}

	public void setCurrentTank(Tank currentTank) {
		this.currentTank = currentTank;
		this.createDensityChart();
	}

	/**
	 * Measure form
	 * @return
	 */
	public List<String> getMeasureTypes(){		
		return Arrays.asList(MEASURE_TYPE.values()).stream().map(e -> e.name()).collect(Collectors.toList());
	}
	
	
	

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public String getMeasureType() {
		return measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
		measure.setMeasureType(MEASURE_TYPE.valueOf(measureType));
	}
	
	public void saveMeasure() {
		 final Contents contents = this.getCurrentTank().getContents();
		 if (contents != null) {
			 contents.getTraceLine().addMeasure(this.measure);
			 this.createDensityChart();
		 }
		 this.clear();
		
	}
	
	/**
	 * Chemicals
	 */
	
	public List<Chemical> searchChemicalContains(String query) {
        String queryLowerCase = query.toLowerCase();       
        List<Chemical> chemicals = caveService.findAllChemicals().stream().filter(t -> t.getCommonName().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
        return chemicals;
    }
	
	public  List<Chemical> getAllChemicals(){
		return  caveService.findAllChemicals();
	}
	
	/**
	 * Charts
	 */
	private void createDensityChart() {
        densityChart = new LineChartModel();
        ChartData data = new ChartData();
        List<String> labels = new ArrayList<>();
      
        /*labels.add("Jour 2");
        labels.add("Jour 3");
        labels.add("Jour 4");
        labels.add("Jour 5");
        labels.add("Jour 6");*/
        data.setLabels(labels);
        densityChart.setData(data);

        //Options
        

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Température et densité (fermentation)");
       
        
        if (this.getCurrentTank() != null) {
	        final Contents contents = this.getCurrentTank().getContents();
	        if (contents != null) {
		        TraceLine traceLine = contents.getTraceLine();
		        List<Measure> measures = traceLine.getMeasures();
		        
		        final Map<MEASURE_TYPE,List<Measure>> measureMap = measures
		        		.stream()
		        		.filter(e -> e.getMeasureType().equals(MEASURE_TYPE.CONTENTS_DENSITY) || e.getMeasureType().equals(MEASURE_TYPE.CONTENTS_TEMPERATURE))
		        		.collect(Collectors.groupingBy(Measure::getMeasureType));
		
		        for (Map.Entry<MEASURE_TYPE,List<Measure>> entry : measureMap.entrySet()) {
		        	   LineChartDataSet dataSet = new LineChartDataSet();
		               List<Object> values = new ArrayList<>();
		               for (Measure measure : entry.getValue()) {
		            	   values.add(measure.getValue());
		            	   String labelValue = new SimpleDateFormat("dd/MM/yyyy").format(measure.getMeasureDate());
		            	   Optional<String> existingLabel = labels.stream().filter(e -> e.equals(labelValue)).findFirst();
		            	   if (existingLabel.isEmpty()) labels.add(labelValue);
		               }
		               	               
		               if (entry.getKey().equals(MEASURE_TYPE.CONTENTS_DENSITY)) {
		            	   dataSet.setYaxisID("left-y-axis");
		               }
		               else {
		            	   dataSet.setYaxisID("right-y-axis");
		               }
		               dataSet.setData(values);
		               dataSet.setLabel(entry.getKey().toString());
		             
		               dataSet.setFill(true);	              
		               data.addChartDataSet(dataSet);
		              
		               
				}
	        }
        }
        
        LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setId("left-y-axis");
        linearAxes.setPosition("left");    
        
        CartesianLinearAxes linearAxes2 = new CartesianLinearAxes();
        linearAxes2.setId("right-y-axis");
        linearAxes2.setPosition("right");
       

        cScales.addYAxesData(linearAxes); 
       
        
        cScales.addYAxesData(linearAxes2);
        options.setScales(cScales);
        options.setTitle(title);

        densityChart.setOptions(options);
        
        /*LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        values.add(1100);
        values.add(1090);
        values.add(1070);
        values.add(1040);
        values.add(1020);
        values.add(980);
        dataSet.setData(values);
        dataSet.setLabel("Densité");
        dataSet.setYaxisID("left-y-axis");
        dataSet.setFill(true);
        //dataSet.setTension(0.5);

        LineChartDataSet dataSet2 = new LineChartDataSet();
        List<Object> values2 = new ArrayList<>();
        values2.add(10);
        values2.add(12);
        values2.add(14);
        values2.add(14);
        values2.add(12);
        values2.add(10);
        dataSet2.setData(values2);
        dataSet2.setLabel("Température");
        dataSet2.setYaxisID("right-y-axis");
        dataSet2.setFill(true);
        dataSet2.setBorderColor("#f3f6f9");
        //dataSet2.setTension(0.5);*/

       // data.addChartDataSet(dataSet);
       // data.addChartDataSet(dataSet2);

       
    }
	
	public LineChartModel getDensityChart() {
		return densityChart;
	}

	public void setDensityChart(LineChartModel cartesianLinerModel) {
		this.densityChart = cartesianLinerModel;
	}

	public List<Chemical> getSelectedChemicals() {
		return selectedChemicals;
	}

	public void setSelectedChemicals(List<Chemical> selectedChemicals) {
		this.selectedChemicals = selectedChemicals;
	}

	
	
}
