package com.shiavnskipayroll.service;


import com.shiavnskipayroll.dto.request.ClientMasterRequestDTO;
import com.shiavnskipayroll.dto.response.ClientMasterResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ClientService {
    void createClient(ClientMasterRequestDTO clientMasterRequestDTO, MultipartFile clientSowSigned)throws IOException, Exception;
    List<ClientMasterResponseDTO> getAllClient();
    ClientMasterResponseDTO getClientById(String id);
    void updateClient(String id, ClientMasterRequestDTO clientMasterRequestDTO, MultipartFile clientSowSigned) throws IOException;
    void deleteClient(String id);
}
