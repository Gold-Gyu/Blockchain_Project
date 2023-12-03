import './NFTGallery.scss';
import React, { ChangeEvent, FormEvent, useState } from 'react';
import {
  Box,
  Toolbar,
  TextField,
  InputAdornment,
  IconButton,
} from '@mui/material';
import CancelIcon from '@mui/icons-material/Cancel';
import SearchIcon from '@mui/icons-material/Search';
import NFTCard from 'components/common/NFTCard/NFTCard';
import NavBar from 'components/common/NavBar/NavBar';

function NFTGalleryForm() {
  const [keyword, setKeyword] = useState('');

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    setKeyword(event.target.value);
  };

  const handleSubmit = async (event: FormEvent) => {
    event?.preventDefault();
    if (!keyword.trim()) {
    }
  };

  return (
    <div className="container">
      <NavBar />
      <h1>GALLERY</h1>
      <Toolbar>
        <Box
          component="form"
          sx={{ width: '100%', display: 'flex', alignItems: 'center' }}
          onSubmit={handleSubmit}
        >
          <TextField
            hiddenLabel
            variant="outlined"
            margin="dense"
            color="success"
            size="small"
            autoFocus
            fullWidth
            placeholder="어떤 NFT를 찾으시나요?"
            value={keyword}
            onChange={handleChange}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  {keyword && (
                    <IconButton type="button" onClick={() => setKeyword('')}>
                      <CancelIcon fontSize="small" />
                    </IconButton>
                  )}
                  <IconButton type="button" onClick={handleSubmit}>
                    <SearchIcon />
                  </IconButton>
                </InputAdornment>
              ),
            }}
            sx={{
              backgroundColor: '#F2F2F7',
              ' .Mui-focused': {
                backgroundColor: 'white',
              },
            }}
          />
        </Box>
      </Toolbar>
      <div className="NFT-list">
        <NFTCard />
        <NFTCard />
        <NFTCard />
      </div>
    </div>
  );
}

export default NFTGalleryForm;
