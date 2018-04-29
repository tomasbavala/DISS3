package com.company.continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import com.company.entity.Zakaznik;
import com.company.simulation.*;
import com.company.agents.*;

//meta! id="41"
public class PlanovacPrichZakPozicovna extends Scheduler {

	private static final int HODINA = 3600;
	private static final double[] vstupy = {12.0, 9.0, 18.0, 28.0, 23.0, 21.0, 16.0, 11.0, 17.0, 22.0, 36.0, 24.0, 32.0, 16.0, 13.0, 13.0, 5.0, 4.0};
	private static ExponentialRNG exponentialRNG1 = new ExponentialRNG(vstupy[0] / HODINA );
	private static ExponentialRNG exponentialRNG2 = new ExponentialRNG(vstupy[1] / HODINA );
	private static ExponentialRNG exponentialRNG3 = new ExponentialRNG(vstupy[2] / HODINA );
	private static ExponentialRNG exponentialRNG4 = new ExponentialRNG(vstupy[3] / HODINA );
	private static ExponentialRNG exponentialRNG5 = new ExponentialRNG(vstupy[4] / HODINA );
	private static ExponentialRNG exponentialRNG6 = new ExponentialRNG(vstupy[5] / HODINA );
	private static ExponentialRNG exponentialRNG7 = new ExponentialRNG(vstupy[6] / HODINA );
	private static ExponentialRNG exponentialRNG8 = new ExponentialRNG(vstupy[7] / HODINA );
	private static ExponentialRNG exponentialRNG9 = new ExponentialRNG(vstupy[8] / HODINA );
	private static ExponentialRNG exponentialRNG10 = new ExponentialRNG(vstupy[9] / HODINA );
	private static ExponentialRNG exponentialRNG11 = new ExponentialRNG(vstupy[10] / HODINA );
	private static ExponentialRNG exponentialRNG12 = new ExponentialRNG(vstupy[11] / HODINA );
	private static ExponentialRNG exponentialRNG13 = new ExponentialRNG(vstupy[12] / HODINA );
	private static ExponentialRNG exponentialRNG14 = new ExponentialRNG(vstupy[13] / HODINA );
	private static ExponentialRNG exponentialRNG15 = new ExponentialRNG(vstupy[14] / HODINA );
	private static ExponentialRNG exponentialRNG16 = new ExponentialRNG(vstupy[15] / HODINA );
	private static ExponentialRNG exponentialRNG17 = new ExponentialRNG(vstupy[16] / HODINA );
	private static ExponentialRNG exponentialRNG18 = new ExponentialRNG(vstupy[17] / HODINA );

	private ExponentialRNG[] genVstupov = {
			exponentialRNG1,exponentialRNG2,exponentialRNG3, exponentialRNG4,
			exponentialRNG5,exponentialRNG6,exponentialRNG7,exponentialRNG8,
			exponentialRNG9,exponentialRNG10,exponentialRNG11,exponentialRNG12,
			exponentialRNG13,exponentialRNG14,exponentialRNG15,exponentialRNG16,
			exponentialRNG17,exponentialRNG18};

    public PlanovacPrichZakPozicovna(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! sender="AgentOkolia", id="42", type="Start"
	public void processStart(MessageForm message) {
		message.setCode(Mc.novyZakaznik);
		hold(dajCasHoldu(), message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! sender="AgentOkolia", id="83", type="Notice"
	public void processNovyZakaznik(MessageForm message) {
		MyMessage msg = new MyMessage((MyMessage) message);
		hold(dajCasHoldu(), msg);

		Zakaznik zakaznik = new Zakaznik(mySim());
		zakaznik.setPrichadzajuci(false);
		((MyMessage)message).setZakaznik(zakaznik);
		assistantFinished(message);
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
		break;

		case Mc.novyZakaznik:
			processNovyZakaznik(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public AgentOkolia myAgent() {
        return (AgentOkolia) super.myAgent();
    }

	private int dajIndexGeneratoraVstupu(double cas){
		return (int)Math.round((cas / (15 * 60))%17);
	}

	private double dajCasHoldu(){
		int indexGenVstupu = dajIndexGeneratoraVstupu(mySim().currentTime());
		double vysledokGeneratora = genVstupov[indexGenVstupu].sample();
		double casPoHolde = mySim().currentTime() + vysledokGeneratora;
		int indexGenVstupuPoHolde = dajIndexGeneratoraVstupu(casPoHolde);

		if (indexGenVstupuPoHolde != indexGenVstupu && vstupy[indexGenVstupu] < vstupy[indexGenVstupuPoHolde]){
			double pom = (((casPoHolde - (indexGenVstupuPoHolde * (15*60))) + genVstupov[indexGenVstupuPoHolde].sample()) / 2) + (((15 * 60)*indexGenVstupuPoHolde)-mySim().currentTime());
			//return (((casPoHolde - (indexGenVstupuPoHolde * (15*60))) + genVstupov[indexGenVstupuPoHolde].sample()) / 2) + (((15 * 60)*indexGenVstupuPoHolde)-mySim().currentTime());
			return pom;
		}else {
			return vysledokGeneratora;
		}
	}

}