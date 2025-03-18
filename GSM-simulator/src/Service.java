
import java.util.ArrayList;
import java.util.List;

public class Service {

	private List<VBD> listOfVBD;
	private List<NodeInterface> inputBTS;
	private List<NodeInterface> outputBTS;
	private List<List<NodeInterface>> listOfBSC;
	private List<VRD> listOfVRD;

	List<AddNodeObserver> adoINBTS;

	List<AddNodeObserver> adoOUTBTS;

	List<AddNodeObserver> adoBSCS;
	List<VBD> exVBD;
	public Service() {
		listOfVBD=new ArrayList<>();
		listOfBSC = new ArrayList<>();
		inputBTS = new ArrayList<>();
		outputBTS = new ArrayList<>();
		listOfVRD = new ArrayList<>();
		adoINBTS = new ArrayList<>();
		adoOUTBTS = new ArrayList<>();
		adoBSCS = new ArrayList<>();
		exVBD = new ArrayList<>();

		inputBTS.add(new INBTS(this));
		outputBTS.add(new OUTBTS(this));
		List<NodeInterface> l = new ArrayList<>();
		l.add(new BSC(this));
		listOfBSC.add(l);
	}

	public List<VBD> getListOfVBD() {
		return listOfVBD;
	}

	public List<NodeInterface> getInputBTS() {
		return inputBTS;
	}

	public List<NodeInterface> getOutputBTS() {
		return outputBTS;
	}

	public List<List<NodeInterface>> getListOfBSC() {
		return listOfBSC;
	}

	public List<VRD> getListOfVRD() {
		return listOfVRD;
	}
	void addVBD(String msg) {
		VBD vbd = new VBD(this);
		vbd.getMessageToPDU(msg);
		listOfVBD.add(vbd);

		exVBD.add(vbd);
	}
	void addVRD() {
		VRD vrd = new VRD(this);
		listOfVRD.add(vrd);
	}
	void updateObesrvers1(INBTS inbts) {
		for( AddNodeObserver observer : this.adoINBTS){
			observer.updateNodePanel1(inbts);
		}
	}
	void updateObservers2(OUTBTS outbts) {
		for (AddNodeObserver element : this.adoOUTBTS){
			element.updateNodePanel2(outbts);
		}
	}
	void updateObservers3(BSC bsc){
		for( AddNodeObserver element : this.adoBSCS){
			element.updateNodePanel3(bsc);
		}
	}
	void addInputBTS(INBTS inbts) {
		this.inputBTS.add(inbts);
		updateObesrvers1(inbts);
	}
	void addOutputBTS(OUTBTS outbts) {
		this.outputBTS.add(outbts);
		updateObservers2(outbts);
	}
	BSC addNewBSCLayer() {
		BSC bsc = new BSC(this);
		List<NodeInterface> newLayer = new ArrayList<>();
		bsc.setNumOfLayer(listOfBSC.size()+1);
		newLayer.add(bsc);
		listOfBSC.add(newLayer);
		return bsc;
	}

	void removeBSCLayer() {
		if(listOfBSC.size() > 1) {
			listOfBSC.remove(listOfBSC.size()-1);
		}else {
			return;
		}
	}
	void addBSC(int num, BSC bsc) {
		if ( num >= listOfBSC.size()) {
			return;
		}
		bsc.setNumOfLayer(num+1);
		listOfBSC.get(num).add(bsc);
		updateObservers3(bsc);
	}

}
