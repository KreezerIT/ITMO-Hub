package infrastructure.presentation.viewmodels;

import application.dtos.TransactionDTO;

import java.util.UUID;

public class TransactionViewModel {
    private UUID id;
    private String date;
    private String type;
    private String amountWithCurrency;

    public void setAmountWithCurrency(String amountWithCurrency) {
        this.amountWithCurrency = amountWithCurrency;
    }

    public static TransactionViewModel fromDTO(TransactionDTO dto) {
        TransactionViewModel viewModel = new TransactionViewModel();
        viewModel.id = dto.id;
        viewModel.date = dto.date.toString();
        viewModel.type = dto.type.toString();
        viewModel.setAmountWithCurrency(dto.usedMoney != null ? dto.usedMoney.getAmount() + " " + dto.usedMoney.getCurrency() : "");
        return viewModel;
    }


    @Override
    public String toString() {
        return "Id: " + id + ", Date: " + date + ", Type: " + type + ", Amount: " + amountWithCurrency;
    }
}
